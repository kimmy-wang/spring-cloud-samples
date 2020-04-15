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

import com.upcwangying.cloud.samples.product.client.hystrix.strategy.RequestAttributeHystrixConcurrencyStrategy;
import com.upcwangying.cloud.samples.product.client.interceptor.AuthInterceptor;
import com.upcwangying.cloud.samples.product.client.interceptor.AuthInterceptor2;
import com.upcwangying.cloud.samples.product.common.entity.ProductInput;
import feign.Logger;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 此接口供其他服务调用
 *
 * @author WANGY
 * @date 2019/3/25 16:47
 */
@FeignClient(name = "product",
        fallback = ProductClient.ProductClientFallback.class,
        configuration = ProductClient.ProductClientConfiguration.class
        )
public interface ProductClient {

    @GetMapping(value = "/products/list/all")
    @ResponseBody
    ResultVO getProductListByProductIds(@RequestParam("productIds") List<String> productIds);

    @PutMapping(value = "/products/save/all")
    @ResponseBody
    ResultVO updateProduct(@RequestBody List<ProductInput> productInputList);

    @Component
    static class ProductClientFallback implements ProductClient {

        @Override
        public ResultVO getProductListByProductIds(List<String> productIds) {
            return null;
        }

        @Override
        public ResultVO updateProduct(List<ProductInput> productInputList) {
            return null;
        }

    }

    @Configuration
    static class ProductClientConfiguration {

        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Bean
        public AuthInterceptor authInterceptor() {
            return new AuthInterceptor();
        }

        @Bean
        public RequestAttributeHystrixConcurrencyStrategy hystrixRequestAutoConfiguration() {
            return new RequestAttributeHystrixConcurrencyStrategy();
        }

        @Bean
        public AuthInterceptor2 authInterceptor2() {
            return new AuthInterceptor2();
        }

        @Bean
        @ConditionalOnBean(AuthInterceptor2.class)
        public okhttp3.OkHttpClient okHttpClient(AuthInterceptor2 authInterceptor2){
            okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS) //读取超时
                    .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                    .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                    .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
                    .addInterceptor(authInterceptor2);
            return ClientBuilder.build();
        }
    }

}
