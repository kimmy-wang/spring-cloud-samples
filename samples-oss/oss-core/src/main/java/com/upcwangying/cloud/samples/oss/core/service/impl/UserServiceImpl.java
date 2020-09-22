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

package com.upcwangying.cloud.samples.oss.core.service.impl;

import com.upcwangying.cloud.samples.oss.core.dao.TokenInfoMapper;
import com.upcwangying.cloud.samples.oss.core.dao.UserInfoMapper;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;
import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author WANGY
 */
@Transactional
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {
    private long LONG_REFRESH_TIME = 4670409600000L;
    private int LONG_EXPIRE_TIME = 36500;
    private String SYSTEM_USER = "super_admin";

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    TokenInfoMapper tokenInfoMapper;

    @Override
    public boolean addUser(UserInfo userInfo) {
        userInfoMapper.addUser(userInfo);
        Date date = new Date();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(userInfo.getUserId());
        tokenInfo.setActive(true);
        tokenInfo.setCreateTime(date);
        tokenInfo.setCreator(SYSTEM_USER);
        tokenInfo.setExpireTime(LONG_EXPIRE_TIME);
        tokenInfo.setRefreshTime(date);
        tokenInfoMapper.addToken(tokenInfo);
        return true;
    }

    @Override
    public boolean updateUser(UserInfo userInfo) {
        if (StringUtils.isEmpty(userInfo.getPassword())) {
            userInfo.setPassword(null);
        }
        userInfoMapper.updateUser(userInfo);
        return true;
    }

    @Override
    public boolean deleteUser(String userId) {
        userInfoMapper.deleteUserById(userId);
        tokenInfoMapper.deleteToken(userId);
        return true;
    }

    @Override
    public UserInfo getUserById(String userId) {
        return userInfoMapper.getUserById(userId);
    }

    @Override
    public UserInfo getUserByName(String userName) {
        return userInfoMapper.getUserByName(userName);
    }

    @Override
    public UserInfo checkPassword(String userName, String password) {
        return userInfoMapper.checkPassword(userName, password);
    }
}
