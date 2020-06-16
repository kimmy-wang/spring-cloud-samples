/*
 *
 * MIT License
 *
 * Copyright (c) 2019 cloud.upcwangying.com
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.upcwangying.cloud.samples.order.web.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.upcwangying.cloud.samples.product.feign.FileUploadClient;
import com.upcwangying.cloud.samples.core.exception.FallbackException;
import com.upcwangying.cloud.samples.order.web.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileUploadClient fileUploadClient;

    @Override
    @HystrixCommand(fallbackMethod = "uploadFallback")
    public String upload(MultipartFile file) {
        String fileName = fileUploadClient.fileUpload(file);
        if (fileName == null) {
            throw new FallbackException();
        }
        return fileName;
    }

    public String uploadFallback(MultipartFile file, Throwable throwable) {
        log.error("[OrderController]: error = {}", throwable.getMessage());
        return null;
    }
}
