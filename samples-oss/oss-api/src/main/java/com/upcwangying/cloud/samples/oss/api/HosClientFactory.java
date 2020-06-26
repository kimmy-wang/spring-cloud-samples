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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WANGY
 */
public class HosClientFactory {

    private static Map<String, OssClient> clientCache = new ConcurrentHashMap<>();

    /**
     * create mosclient by endpoints and token.
     *
     * @param endpoints seperated by comma,eg:http://127.0.0.1:80801,http://127.0.0.1:80802
     * @param token auth token
     * @return client
     */
    public static OssClient getOrClient(String endpoints, String token) {
        String key = endpoints + "_" + token;
        if (clientCache.containsKey(key)) {
            return clientCache.get(key);
        } else {
            OssClient client = new OssClientImpl(endpoints, token);
            clientCache.put(key, client);
            return client;
        }

    }
}
