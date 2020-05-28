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

package com.upcwangying.cloud.samples.product.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@FeignClient(value = "product",
        fallback = FileUploadClient.FileUploadClientFallback.class,
        configuration = FileUploadClient.MultipartSupportConfig.class)
public interface FileUploadClient {

    /***
     * 1.produces,consumes必填
     * 2.注意区分@RequestPart和RequestParam，不要将
     * @RequestPart(value = "file") 写成@RequestParam(value = "file")
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile/server",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String fileUpload(@RequestPart(value = "file") MultipartFile file);

    @Component
    static class FileUploadClientFallback implements FileUploadClient {

        @Override
        public String fileUpload(MultipartFile file) {
            return null;
        }
    }

    /**
     * See {@link <a href="https://github.com/OpenFeign/feign-form"></a>}
     */
    @Component
    static class MultipartSupportConfig {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignFormEncoder () {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }

//        @Bean
//        public Encoder feignFormEncoder () {
//            return new SpringFormEncoder();
//        }

//        @Bean
//        public Decoder feignDecoder () {
//            List<HttpMessageConverter<?>> springConverters =
//                    messageConverters.getObject().getConverters();
//
//            List<HttpMessageConverter<?>> decoderConverters =
//                    new ArrayList<HttpMessageConverter<?>>(springConverters.size() + 1);
//
//            decoderConverters.addAll(springConverters);
//            decoderConverters.add(new SpringManyMultipartFilesReader(4096));
//
//            HttpMessageConverters httpMessageConverters = new HttpMessageConverters(decoderConverters);
//
//            return new SpringDecoder(() -> httpMessageConverters);
//        }
    }

}
