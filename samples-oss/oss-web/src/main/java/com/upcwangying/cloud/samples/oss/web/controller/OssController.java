/*
 *
 * MIT License
 *
 * Copyright (c) 2020 cloud.upcwangying.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.upcwangying.cloud.samples.oss.web.controller;

import com.google.common.base.Splitter;
import com.upcwangying.cloud.samples.oss.common.*;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.enums.SystemRole;
import com.upcwangying.cloud.samples.oss.server.service.IBucketService;
import com.upcwangying.cloud.samples.oss.server.service.IOssStoreService;
import com.upcwangying.cloud.samples.oss.web.security.ContextUtil;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author WANGY
 */
@RestController
@RequestMapping("oss/v1/")
public class OssController extends BaseController {

    private static Logger logger = Logger.getLogger(OssController.class);

    @Autowired
    @Qualifier("bucketServiceImpl")
    IBucketService bucketService;

    @Autowired
    @Qualifier("ossStoreService")
    IOssStoreService ossStoreService;

    private static long MAX_FILE_IN_MEMORY = 2 * 1024 * 1024;

    private final int readBufferSize = 32 * 1024;

    private static String TMP_DIR = System.getProperty("user.dir") + File.separator + "tmp";

    public OssController() {
        File file = new File(TMP_DIR);
        file.mkdirs();
    }

    @RequestMapping(value = "bucket", method = RequestMethod.POST)
    public Object createBucket(@RequestParam("bucket") String bucketName,
                               @RequestParam(name = "detail", required = false, defaultValue = "") String detail) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!currentUser.getSystemRole().equals(SystemRole.GUEST)) {
            bucketService.addBucket(currentUser, bucketName, detail);
            try {
                ossStoreService.createBucketStore(bucketName);
            } catch (IOException ioe) {
                bucketService.deleteBucket(bucketName);
                return  "create bucket error";
            }
            return "success";
        }
        return "PERMISSION DENIED";
    }

    @RequestMapping(value = "bucket", method = RequestMethod.DELETE)
    public Object deleteBucket(@RequestParam("bucket") String bucket) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkBucketOwner(currentUser.getUserName(), bucket)) {
            try {
                ossStoreService.deleteBucketStore(bucket);
            } catch (IOException ioe) {
                return "delete bucket error";
            }
            bucketService.deleteBucket(bucket);
            return "success";
        }
        return "PERMISSION DENIED";
    }

    @RequestMapping(value = "bucket", method = RequestMethod.PUT)
    public Object updateBucket(
            @RequestParam(name = "bucket") String bucket,
            @RequestParam(name = "detail") String detail) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        BucketModel bucketModel = bucketService.getBucketByName(bucket);
        if (operationAccessControl
                .checkBucketOwner(currentUser.getUserName(), bucketModel.getBucketName())) {
            bucketService.updateBucket(bucket, detail);
            return "success";
        }
        return "PERMISSION DENIED";
    }

    @RequestMapping(value = "bucket", method = RequestMethod.GET)
    public Object getBucket(@RequestParam(name = "bucket") String bucket) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        BucketModel bucketModel = bucketService.getBucketByName(bucket);
        if (operationAccessControl
                .checkPermission(currentUser.getUserId(), bucketModel.getBucketName())) {
            return bucketModel;
        }
        return "PERMISSION DENIED";

    }

    @RequestMapping(value = "bucket/list", method = RequestMethod.GET)
    public Object getBucket() {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        List<BucketModel> bucketModels = bucketService.getUserBuckets(currentUser.getUserId());
        return bucketModels;
    }

    @RequestMapping(value = "object", method = {RequestMethod.PUT, RequestMethod.POST})
    @ResponseBody
    public Object putObject(@RequestParam("bucket") String bucket,
                            @RequestParam("key") String key,
                            @RequestParam(value = "mediaType", required = false) String mediaType,
                            @RequestParam(value = "content", required = false) MultipartFile file,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return "Permission denied";
        }
        if (!key.startsWith("/")) {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.getWriter().write("object key must start with /");
        }

        Enumeration<String> headNames = request.getHeaderNames();
        Map<String, String> attrs = new HashMap<>();
        String contentEncoding = request.getHeader("content-encoding");
        if (contentEncoding != null) {
            attrs.put("content-encoding", contentEncoding);
        }
        while (headNames.hasMoreElements()) {
            String header = headNames.nextElement();
            if (header.startsWith(OssHeaders.COMMON_ATTR_PREFIX)) {
                attrs.put(header.replace(OssHeaders.COMMON_ATTR_PREFIX, ""), request.getHeader(header));
            }
        }
        ByteBuffer buffer = null;
        File distFile = null;
        try {
            //put dir object
            if (key.endsWith("/")) {
                if (file != null) {
                    response.setStatus(HttpStatus.SC_BAD_REQUEST);
                    file.getInputStream().close();
                    return null;
                }
                ossStoreService.put(bucket, key, null, 0, mediaType, attrs);
                response.setStatus(HttpStatus.SC_OK);
                return "success";
            }
            if (file == null || file.getSize() == 0) {
                response.setStatus(HttpStatus.SC_BAD_REQUEST);
                response.getWriter().write("object content could not be empty");
                return "object content could not be empty";
            }

            if (file != null) {
                if (file.getSize() > MAX_FILE_IN_MEMORY) {
                    distFile = new File(TMP_DIR + File.separator + UUID.randomUUID().toString());
                    file.transferTo(distFile);
                    file.getInputStream().close();
                    buffer = new FileInputStream(distFile).getChannel()
                            .map(FileChannel.MapMode.READ_ONLY, 0, file.getSize());
                } else {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    org.apache.commons.io.IOUtils.copy(file.getInputStream(), outputStream);
                    buffer = ByteBuffer.wrap(outputStream.toByteArray());
                    file.getInputStream().close();
                }
            }
            ossStoreService.put(bucket, key, buffer, file.getSize(), mediaType, attrs);
            return "success";
        } catch (IOException ioe) {
            logger.error(ioe);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("server error");
            return "server error";
        } finally {
            if (buffer != null) {
                buffer.clear();
            }
            if (file != null) {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    //nothing to do
                }
            }
            if (distFile != null) {
                distFile.delete();
            }
        }
    }

    @RequestMapping(value = "object/list", method = RequestMethod.GET)
    public ObjectListResult listObject(@RequestParam("bucket") String bucket,
                                       @RequestParam("startKey") String startKey,
                                       @RequestParam("endKey") String endKey,
                                       HttpServletResponse response)
            throws IOException {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return null;
        }
        if (startKey.compareTo(endKey) > 0) {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        ObjectListResult result = new ObjectListResult();
        List<OssObjectSummary> summaryList = ossStoreService.list(bucket, startKey, endKey);
        result.setBucket(bucket);
        if (summaryList.size() > 0) {
            result.setMaxKey(summaryList.get(summaryList.size() - 1).getKey());
            result.setMinKey(summaryList.get(0).getKey());
        }
        result.setObjectCount(summaryList.size());
        result.setObjectList(summaryList);
        return result;
    }

    @RequestMapping(value = "object/info", method = RequestMethod.GET)
    public OssObjectSummary getSummary(String bucket, String key, HttpServletResponse response)
            throws IOException {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return null;
        }
        OssObjectSummary summary = ossStoreService.getSummary(bucket, key);
        if (summary == null) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
        return summary;
    }

    @RequestMapping(value = "object/list/prefix", method = RequestMethod.GET)
    public ObjectListResult listObjectByPrefix(@RequestParam("bucket") String bucket,
                                               @RequestParam("dir") String dir,
                                               @RequestParam("prefix") String prefix,
                                               @RequestParam(value = "startKey", required = false, defaultValue = "") String start,
                                               HttpServletResponse response)
            throws IOException {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return null;
        }
        if (!dir.startsWith("/") || !dir.endsWith("/")) {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.getWriter().write("dir must start with / and end with /");
            return null;
        }
        if ("".equals(start) || start.equals("/")) {
            start = null;
        }
        if (start != null) {
            List<String> segs = StreamSupport.stream(Splitter
                    .on("/")
                    .trimResults()
                    .omitEmptyStrings().split(start).spliterator(), false).collect(Collectors.toList());
            start = segs.get(segs.size() - 1);
        }
        ObjectListResult result = this.ossStoreService.listByPrefix(bucket, dir, prefix, start, 100);
        return result;
    }

    @RequestMapping(value = "object/list/dir", method = RequestMethod.GET)
    public ObjectListResult listObjectByDir(@RequestParam("bucket") String bucket,
                                            @RequestParam("dir") String dir,
                                            @RequestParam(value = "startKey", required = false, defaultValue = "") String start,
                                            HttpServletResponse response)
            throws Exception {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return null;
        }
        if (!dir.startsWith("/") || !dir.endsWith("/")) {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.getWriter().write("dir must start with / and end with /");
            return null;
        }
        if ("".equals(start) || start.equals("/")) {
            start = null;
        }
        if (start != null) {
            List<String> segs = StreamSupport.stream(Splitter
                    .on("/")
                    .trimResults()
                    .omitEmptyStrings().split(start).spliterator(), false).collect(Collectors.toList());
            start = segs.get(segs.size() - 1);
        }

        ObjectListResult result = this.ossStoreService.listDir(bucket, dir, start, 100);
        return result;
    }


    @RequestMapping(value = "object", method = RequestMethod.DELETE)
    public Object deleteObject(@RequestParam("bucket") String bucket,
                               @RequestParam("key") String key) throws Exception {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            return  "PERMISSION DENIED";
        }
        this.ossStoreService.deleteObject(bucket, key);
        return "success";
    }

    @RequestMapping(value = "object/content", method = RequestMethod.GET)
    public void getObject(@RequestParam("bucket") String bucket,
                          @RequestParam("key") String key, HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!operationAccessControl.checkPermission(currentUser.getUserId(), bucket)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            response.getWriter().write("Permission denied");
            return;
        }
        OssObject object = this.ossStoreService.getObject(bucket, key);
        if (object == null) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
            return;
        }
        response.setHeader(OssHeaders.COMMON_OBJ_BUCKET, bucket);
        response.setHeader(OssHeaders.COMMON_OBJ_KEY, key);
        response.setHeader(OssHeaders.RESPONSE_OBJ_LENGTH, "" + object.getMetaData().getLength());
        String iflastModify = request.getHeader("If-Modified-Since");
        String lastModify = object.getMetaData().getLastModifyTime() + "";
        response.setHeader("Last-Modified", lastModify);
        String contentEncoding = object.getMetaData().getContentEncoding();
        if (contentEncoding != null) {
            response.setHeader("content-encoding", contentEncoding);
        }
        if (iflastModify != null && iflastModify.equals(lastModify)) {
            response.setStatus(HttpStatus.SC_NOT_MODIFIED);
            return;
        }
        response.setHeader(OssHeaders.COMMON_OBJ_BUCKET, object.getMetaData().getBucket());
        response.setContentType(object.getMetaData().getMediaType());
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = object.getContent();
        try {
            byte[] buffer = new byte[readBufferSize];
            int len = -1;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            response.flushBuffer();
        } finally {
            inputStream.close();
            outputStream.close();
        }

    }
}
