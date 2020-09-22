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

package com.upcwangying.cloud.samples.oss.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WANGY
 */
public class JsonUtil {
    private static ObjectMapper objMapper = new ObjectMapper();

    static {
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        objMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                false);
    }

    public static String toJson(Object obj) throws IOException {
        return objMapper.writeValueAsString(obj);
    }


    public static String toJson(Object obj, boolean prettyFormat) throws IOException {
        String json = objMapper.writeValueAsString(obj);
        if (prettyFormat) {
            Object jsonObj = objMapper.readValue(json, Object.class);
            json = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        }
        return json;
    }


    public static <T> T fromJson(Class<T> clazz, String json)
            throws IOException {
        return objMapper.readValue(json, clazz);
    }

    public static <T> List<T> fromJsonList(Class<T> clazz, String json)
            throws IOException {
        return objMapper.readValue(json, objMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz));
    }

    public static <K, V> Map<K, V> fromJsonMap(Class<K> keyClazz, Class<V> valueClazz, String json)
            throws IOException {
        return objMapper.readValue(json, objMapper.getTypeFactory()
                .constructMapType(HashMap.class, keyClazz, valueClazz));
    }

}
