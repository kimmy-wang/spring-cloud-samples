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

package com.upcwangying.cloud.samples.oss.api;

import com.upcwangying.cloud.samples.oss.common.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author WANGY
 */
public interface OssClient {
    void createBucket(String bucketName) throws IOException;

    void deleteBucket(String bucketName) throws IOException;

    List<BucketModel> listBucket() throws IOException;

    void putObject(PutRequest putRequest) throws IOException;

    void putObject(String bucket, String key) throws IOException;

    void putObject(String bucket, String key, byte[] content, String mediaType) throws IOException;

    OssObjectSummary getObjectSummary(String bucket, String key) throws IOException;

    void deleteObject(String bucket, String key) throws IOException;

    void putObject(String bucket, String key, byte[] content, String mediaType,
                          String contentEncoding) throws IOException;

    void putObject(String bucket, String key, File content, String mediaType) throws IOException;

    void putObject(String bucket, String key, File content, String mediaType, String contentEncoding) throws IOException;

    void putObject(String bucket, String key, File content) throws IOException;

    ObjectListResult listObject(String bucket, String startKey, String endKey) throws IOException;

    ObjectListResult listObject(ListObjectRequest request) throws IOException;

    ObjectListResult listObjectByPrefix(String bucket, String dir, String prefix, String startKey) throws IOException;

    ObjectListResult listObjectByDir(String bucket, String dir, String startKey) throws IOException;

    OssObject getObject(String bucket, String key) throws IOException;

    void createBucket(String bucketName, String detail) throws IOException;

    BucketModel getBucketInfo(String bucketName) throws IOException;
}
