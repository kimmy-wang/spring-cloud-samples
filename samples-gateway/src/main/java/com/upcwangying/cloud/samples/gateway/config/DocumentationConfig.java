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

package com.upcwangying.cloud.samples.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger-ui配置类
 *
 * @author WANGY
 * @date 2019/3/29 14:15
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    private static final String API_URL = "/v2/api-docs";
    private static final String SWAGGER_VERSION = "2.0";
    private static final String PREFIX_API_URL = "/%s%s";

    @Autowired
    private Swagger2Properties swagger2Properties;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        final List<String> services = discoveryClient.getServices();
        final List<String> swaggerExcludeServices = swagger2Properties.getExcludeServices();
        return services.stream().filter(e -> {
            if (null != swaggerExcludeServices && swaggerExcludeServices.size() > 0) {
                return !swaggerExcludeServices.contains(e);
            }
            return true;
        }).map(e ->
                swaggerResource(e, String.format(PREFIX_API_URL, e, API_URL))
        ).collect(Collectors.toList());
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(SWAGGER_VERSION);
        return swaggerResource;
    }
}
