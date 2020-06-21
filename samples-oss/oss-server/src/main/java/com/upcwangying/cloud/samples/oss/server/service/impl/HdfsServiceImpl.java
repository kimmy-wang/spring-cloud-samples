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

package com.upcwangying.cloud.samples.oss.server.service.impl;

import com.upcwangying.cloud.samples.oss.core.config.OssConfiguration;
import com.upcwangying.cloud.samples.oss.server.service.IHdfsService;
import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author WANGY
 */
public class HdfsServiceImpl implements IHdfsService {
    private static Logger logger = Logger.getLogger(HdfsServiceImpl.class);
    private FileSystem fileSystem;
    private long defaultBlockSize = 128 * 1024 * 1024;
    private long initBlockSize = defaultBlockSize / 2;

    public HdfsServiceImpl() throws Exception {
        String confDir = System.getenv("HADOOP_CONF_DIR");
        if (confDir == null) {
            confDir = System.getProperty("HADOOP_CONF_DIR");
        }
        if (confDir == null) {
            OssConfiguration ossConfiguration = OssConfiguration.getInstance();
            confDir = ossConfiguration.getString("hadoop.conf.dir");
        }
        if (!new File(confDir).exists()) {
            throw new FileNotFoundException(confDir);
        }
        Configuration conf = new Configuration();
        conf.addResource(new Path(confDir + "/core-site.xml"));
        conf.addResource(new Path(confDir + "/hdfs-site.xml"));
//    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
//    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        fileSystem = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
    }

    @Override
    public void saveFile(String dir, String name,
                         InputStream input, long length, short replication) throws IOException {
        Path dirPath = new Path(dir);
        try {
            if (!fileSystem.exists(dirPath)) {
                boolean succ = fileSystem.mkdirs(dirPath, FsPermission.getDirDefault());
                logger.info("create dir " + dirPath + " success" + succ);
                if (!succ) {
                    throw new IOException("dir create failed:" + dir);
                }
            }
        } catch (FileExistsException ex) {
            //do nothing
        }
        Path path = new Path(dir + "/" + name);
        long blockSize = length <= initBlockSize ? initBlockSize : defaultBlockSize;
        FSDataOutputStream outputStream =
                fileSystem.create(path, true, 512 * 1024, replication, blockSize);
        try {
            fileSystem.setPermission(path, FsPermission.getFileDefault());
            byte[] buffer = new byte[512 * 1024];
            int len = -1;
            while ((len = input.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } finally {
            input.close();
            outputStream.close();
        }
    }

    @Override
    public void deleteFile(String dir, String name) throws IOException {
        fileSystem.delete(new Path(dir + "/" + name), false);
    }

    @Override
    public InputStream openFile(String dir, String name) throws IOException {
        return fileSystem.open(new Path(dir + "/" + name));
    }

    @Override
    public void mikDir(String dir) throws IOException {
        fileSystem.mkdirs(new Path(dir));
    }

    @Override
    public void deleteDir(String dir) throws IOException {
        this.fileSystem.delete(new Path(dir), true);
    }
}
