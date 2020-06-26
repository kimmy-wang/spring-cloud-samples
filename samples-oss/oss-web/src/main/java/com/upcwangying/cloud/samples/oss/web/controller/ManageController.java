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

import com.upcwangying.cloud.samples.oss.core.entity.ServiceAuth;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.enums.SystemRole;
import com.upcwangying.cloud.samples.oss.core.exception.ErrorCodes;
import com.upcwangying.cloud.samples.oss.core.service.IAuthService;
import com.upcwangying.cloud.samples.oss.core.service.IUserService;
import com.upcwangying.cloud.samples.oss.web.security.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WANGY
 */
@RestController
@RequestMapping("hos/v1/sys/")
public class ManageController extends BaseController {

    @Autowired
    @Qualifier("authServiceImpl")
    IAuthService authService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public Object createUser(@RequestParam("userName") String userName,
                             @RequestParam("password") String password,
                             @RequestParam(name = "detail", required = false, defaultValue = "") String detail,
                             @RequestParam(name = "role", required = false, defaultValue = "USER") String role) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl
                .checkSystemRole(currentUser.getSystemRole(), SystemRole.valueOf(role))) {
            UserInfo userInfo = new UserInfo(userName, password, detail, SystemRole.valueOf(role));
            userService.addUser(userInfo);
            return getResult("success");
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "NOT ADMIN");
    }

    @RequestMapping(value = "user", method = RequestMethod.DELETE)
    public Object deleteUser(@RequestParam("userId") String userId) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkSystemRole(currentUser.getSystemRole(), userId)) {
            userService.deleteUser(userId);
            return getResult("success");
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }

    @RequestMapping(value = "user", method = RequestMethod.PUT)
    public Object updateUserInfo(
            @RequestParam(name = "password", required = false, defaultValue = "") String password,
            @RequestParam(name = "detail", required = false, defaultValue = "") String detail) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (currentUser.getSystemRole().equals(SystemRole.GUEST)) {
            return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
        }

        userService.updateUser(new UserInfo(currentUser.getUserId(), password, detail, currentUser.getSystemRole()));
        return getResult("success");
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public Object getUserInfo() {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        return getResult(currentUser);
    }

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public Object createToken(
            @RequestParam(name = "expireTime", required = false, defaultValue = "7") String expireTime,
            @RequestParam(name = "isActive", required = false, defaultValue = "true") String isActive) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!currentUser.getSystemRole().equals(SystemRole.GUEST)) {
            TokenInfo tokenInfo = new TokenInfo(currentUser.getUserName());
            tokenInfo.setExpireTime(Integer.parseInt(expireTime));
            tokenInfo.setActive(Boolean.parseBoolean(isActive));
            authService.addToken(tokenInfo);
            return getResult(tokenInfo);
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "NOT USER");
    }

    @RequestMapping(value = "token", method = RequestMethod.DELETE)
    public Object deleteToken(@RequestParam("token") String token) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkTokenOwner(currentUser.getUserName(), token)) {
            authService.deleteToken(token);
            return getResult("success");
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }

    @RequestMapping(value = "token", method = RequestMethod.PUT)
    public Object updateTokenInfo(
            @RequestParam("token") String token,
            @RequestParam(name = "expireTime", required = false, defaultValue = "7") String expireTime,
            @RequestParam(name = "isActive", required = false, defaultValue = "true") String isActive) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkTokenOwner(currentUser.getUserName(), token)) {
            authService.updateToken(token, Integer.parseInt(expireTime), Boolean.parseBoolean(isActive));
            return getResult("success");
        }

        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public Object getTokenInfo(@RequestParam("token") String token) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkTokenOwner(currentUser.getUserName(), token)) {
            TokenInfo tokenInfo = authService.getTokenInfo(token);
            return getResult(tokenInfo);
        }

        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");

    }

    @RequestMapping(value = "token/list", method = RequestMethod.GET)
    public Object getTokenInfoList() {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (!currentUser.getSystemRole().equals(SystemRole.GUEST)) {
            List<TokenInfo> tokenInfos = authService.getTokenInfos(currentUser.getUserName());
            return getResult(tokenInfos);
        }

        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");

    }

    @RequestMapping(value = "token/refresh", method = RequestMethod.POST)
    public Object refreshToken(@RequestParam("token") String token) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl.checkTokenOwner(currentUser.getUserName(), token)) {
            authService.refreshToken(token);
            return getResult("success");
        }

        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public Object createAuth(@RequestBody ServiceAuth serviceAuth) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl
                .checkBucketOwner(currentUser.getUserName(), serviceAuth.getBucketName())
                && operationAccessControl
                .checkTokenOwner(currentUser.getUserName(), serviceAuth.getTargetToken())) {
            authService.addAuth(serviceAuth);
            return getResult("success");
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }

    @RequestMapping(value = "auth", method = RequestMethod.DELETE)
    public Object deleteAuth(@RequestParam("bucket") String bucket,
                             @RequestParam("token") String token) {
        UserInfo currentUser = ContextUtil.getCurrentUser();
        if (operationAccessControl
                .checkBucketOwner(currentUser.getUserName(), bucket)
                && operationAccessControl
                .checkTokenOwner(currentUser.getUserName(), token)) {
            authService.deleteAuth(bucket, token);
            return getResult("success");
        }
        return getError(ErrorCodes.ERROR_PERMISSION_DENIED, "PERMISSION DENIED");
    }
}
