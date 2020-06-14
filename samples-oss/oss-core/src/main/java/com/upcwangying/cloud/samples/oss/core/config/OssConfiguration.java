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

package com.upcwangying.cloud.samples.oss.core.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author WANGY
 */
public class OssConfiguration {
    private Properties properties;
    private OssConfiguration() {
    }

    private static class OssConfigurationHolder {
        private static final OssConfiguration INSTANCE;

        static {
            INSTANCE = new OssConfiguration();
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            INSTANCE.properties = new Properties();

            try {
                Resource[] resources = resourcePatternResolver.getResources("classpath:*.properties");
                for (Resource resource: resources) {
                    Properties prop = new Properties();
                    InputStream inputStream = resource.getInputStream();
                    prop.load(inputStream);
                    INSTANCE.properties.putAll(prop);
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static OssConfiguration getInstance() {
        return OssConfigurationHolder.INSTANCE;
    }

    public static Properties getProperties() {
        return getInstance().properties;
    }

    public String getString(String key) {
        return getProperties().get(key).toString();
    }

    public int getInt(String key) {
        return Integer.parseInt(getProperties().get(key).toString());
    }
}
