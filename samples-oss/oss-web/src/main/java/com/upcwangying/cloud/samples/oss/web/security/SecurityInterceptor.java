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

package com.upcwangying.cloud.samples.oss.web.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.enums.SystemRole;
import com.upcwangying.cloud.samples.oss.core.service.IAuthService;
import com.upcwangying.cloud.samples.oss.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author WANGY
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor {


    @Autowired
    @Qualifier("authServiceImpl")
    private IAuthService authService;

    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService userService;

    private Cache<String, UserInfo> userInfoCache =
            CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.MINUTES).build();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getRequestURI().equals("/loginPost")) {
            return true;
        }
        String token = "";
        HttpSession session = request.getSession();
        if (session.getAttribute(ContextUtil.SESSION_KEY) != null) {
            token = session.getAttribute(ContextUtil.SESSION_KEY).toString();
        } else {
            token = request.getHeader("X-Auth-Token");
        }
        TokenInfo tokenInfo = authService.getTokenInfo(token);
        if (tokenInfo == null) {
            String url = "/loginPost";
            response.sendRedirect(url);
            //response.setStatus(403);
            return false;
        }
        UserInfo userInfo = userInfoCache.getIfPresent(tokenInfo.getToken());
        if (userInfo == null) {
            userInfo = userService.getUserById(token);
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setUserId(token);
                userInfo.setUserName("NOT_EXIST_USER");
                userInfo.setDetail("a temporary visitor");
                userInfo.setSystemRole(SystemRole.GUEST);
            }
            userInfoCache.put(tokenInfo.getToken(), userInfo);
        }
        ContextUtil.setCurrentUser(userInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
}

