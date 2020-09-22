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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WANGY
 */
class MimeUtil {

    private static Map<String, String> mimeMap = new HashMap<>();

    static {
        try {
            InputStream inputStream = MimeUtil.class.getResourceAsStream("/mime.types");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] ss = line.split("\\s+", 2);
                String ext = ss[1].trim();
                String[] exts = ext.substring(0, ext.length() - 1).split("\\s+");
                for (String extension : exts) {
                    mimeMap.put(extension, ss[0].trim());
                }
            }
        } catch (IOException e) {
            //nothing to do
        }

    }

    public static String getFileMimeType(String extension) {
        return mimeMap.get(extension);
    }

    public static String getFileMimeType(File file) {
        String name = file.getName();
        String mine = "application/octet-stream";
        if (name.lastIndexOf(".") > 0) {
            String ext = name.substring(name.lastIndexOf(".") + 1);
            String extMime = getFileMimeType(ext);
            if (extMime != null) {
                mine = extMime;
            }
        }
        return mine;
    }

}
