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

import com.upcwangying.cloud.samples.oss.common.BucketModel;
import com.upcwangying.cloud.samples.oss.core.entity.ServiceAuth;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.enums.SystemRole;
import com.upcwangying.cloud.samples.oss.core.service.IAuthService;
import com.upcwangying.cloud.samples.oss.core.service.IUserService;
import com.upcwangying.cloud.samples.oss.core.utils.CoreUtil;
import com.upcwangying.cloud.samples.oss.server.service.IBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author WANGY
 */
@Component("DefaultAccessControl")
public class DefaultOperationAccessControl implements IOperationAccessControl {

    @Autowired
    @Qualifier("authServiceImpl")
    IAuthService authService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Autowired
    @Qualifier("bucketServiceImpl")
    IBucketService bucketService;

    @Override
    public UserInfo checkLogin(String userName, String password) {
        UserInfo userInfo = userService.getUserByName(userName);
        if (userInfo == null) {
            return null;
        } else {
            return userInfo.getPassword().equals(CoreUtil.getMd5Password(password)) ? userInfo : null;
        }

    }

    @Override
    public boolean checkSystemRole(SystemRole systemRole1, SystemRole systemRole2) {
        if (systemRole1.equals(SystemRole.SUPER_ADMIN)) {
            return true;
        }
        return systemRole1.equals(SystemRole.ADMIN) && systemRole2.equals(SystemRole.USER);
    }

    @Override
    public boolean checkTokenOwner(String userName, String token) {
        TokenInfo tokenInfo = authService.getTokenInfo(token);
        return tokenInfo.getCreator().equals(userName);
    }

    @Override
    public boolean checkSystemRole(SystemRole systemRole1, String userId) {
        if (systemRole1.equals(SystemRole.SUPER_ADMIN)) {
            return true;
        }
        UserInfo userInfo = userService.getUserById(userId);
        return systemRole1.equals(SystemRole.ADMIN) && userInfo.getSystemRole().equals(SystemRole.USER);
    }

    @Override
    public boolean checkBucketOwner(String userName, String bucketName) {
        BucketModel bucketModel = bucketService.getBucketByName(bucketName);
        return bucketModel.getCreator().equals(userName);
    }

    @Override
    public boolean checkPermission(String token, String bucket) {
        if (authService.checkToken(token)) {
            ServiceAuth serviceAuth = authService.getServiceAuth(bucket, token);
            if (serviceAuth != null) {
                return true;
            }
        }
        return false;
    }
}

