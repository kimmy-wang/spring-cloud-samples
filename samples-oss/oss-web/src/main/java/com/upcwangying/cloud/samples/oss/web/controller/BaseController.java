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

package com.upcwangying.cloud.samples.oss.web.controller;

import com.upcwangying.cloud.samples.oss.core.exception.ErrorCodes;
import com.upcwangying.cloud.samples.oss.core.exception.OssException;
import com.upcwangying.cloud.samples.oss.web.security.IOperationAccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WANGY
 */
@ControllerAdvice
public class BaseController {

    @Autowired
    @Qualifier("DefaultAccessControl")
    protected IOperationAccessControl operationAccessControl;

    /**
     * 异常处理.
     */
    @ExceptionHandler
    @ResponseBody
    public Map<String, Object> exceptionHandle(Exception ex, HttpServletResponse response) {
        ex.printStackTrace();
        if (OssException.class.isAssignableFrom(ex.getClass())) {
            OssException hosException = (OssException) ex;
            if (hosException.code() == ErrorCodes.ERROR_PERMISSION_DENIED) {
                response.setStatus(403);
            } else {
                response.setStatus(500);
            }
            return getResultMap(hosException.code(), null, hosException.getMsg(), null);
        } else {
            response.setStatus(500);
            return getResultMap(500, null, ex.getMessage(), null);
        }
    }

    protected Map<String, Object> getResult(Object object) {
        return getResultMap(null, object, null, null);
    }


    protected Map<String, Object> getResult(Object object, Map<String, Object> extraMap) {
        return getResultMap(null, object, null, extraMap);
    }

    protected Map<String, Object> getError(int errCode, String errMsg) {
        return getResultMap(errCode, null, errMsg, null);
    }

    private Map<String, Object> getResultMap(Integer code, Object data, String msg,
                                             Map<String, Object> extraMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (code == null || code.equals(200)) {
            map.put("code", 200);
            map.put("data", data);
        } else {
            map.put("code", code);
            map.put("msg", msg);
        }
        if (extraMap != null && !extraMap.isEmpty()) {
            map.putAll(extraMap);
        }
        return map;
    }

}
