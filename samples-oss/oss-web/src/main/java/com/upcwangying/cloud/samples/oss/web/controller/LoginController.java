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

import com.google.common.base.Strings;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.exception.ErrorCodes;
import com.upcwangying.cloud.samples.oss.web.security.ContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author WANGY
 */
@Controller
public class LoginController extends BaseController {

    @RequestMapping("/")
    public String logIn(HttpServletRequest request) {
        return "index.html";
    }

    @RequestMapping("/loginPost")
    @ResponseBody
    public Object loginPost(String username, String password, HttpSession session)
            throws IOException {
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "username or password can not be null");
        }
        UserInfo userInfo = operationAccessControl.checkLogin(username, password);
        if (userInfo != null) {
            session.setAttribute(ContextUtil.SESSION_KEY, userInfo.getUserId());
            return getResult("success");
        } else {
            return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "login error");
        }
    }

    @GetMapping("/logout")
    @ResponseBody
    public Object logout(HttpSession session) {
        session.removeAttribute(ContextUtil.SESSION_KEY);
        return getResult("success");
    }
}
