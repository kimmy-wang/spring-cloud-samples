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

import com.upcwangying.cloud.samples.oss.core.dao.ServiceAuthMapper;
import com.upcwangying.cloud.samples.oss.core.dao.TokenInfoMapper;
import com.upcwangying.cloud.samples.oss.core.entity.ServiceAuth;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;
import com.upcwangying.cloud.samples.oss.core.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author WANGY
 */
@Transactional
@Service("authServiceImpl")
public class AuthServiceImpl implements IAuthService {

    @Autowired
    TokenInfoMapper tokenInfoMapper;
    @Autowired
    ServiceAuthMapper serviceAuthMapper;

    @Override
    public boolean addAuth(ServiceAuth auth) {
        serviceAuthMapper.addAuth(auth);
        return true;
    }

    @Override
    public boolean deleteAuth(String bucketName, String token) {
        serviceAuthMapper.deleteAuth(bucketName, token);
        return true;
    }

    @Override
    public boolean deleteAuthByBucket(String bucketName) {
        serviceAuthMapper.deleteAuthByBucket(bucketName);
        return true;
    }

    @Override
    public boolean deleteAuthByToken(String token) {
        serviceAuthMapper.deleteAuthByToken(token);
        return true;
    }

    @Override
    public ServiceAuth getServiceAuth(String bucketName, String token) {
        return serviceAuthMapper.getAuth(bucketName, token);
    }

    @Override
    public boolean addToken(TokenInfo tokenInfo) {
        tokenInfoMapper.addToken(tokenInfo);
        return true;
    }


    @Override
    public boolean updateToken(String token, int expireTime, boolean isActive) {
        tokenInfoMapper.updateToken(token, expireTime, isActive ? 1 : 0);
        return true;
    }

    @Override
    public boolean refreshToken(String token) {
        tokenInfoMapper.refreshToken(token, new Date());
        return true;
    }

    @Override
    public boolean deleteToken(String token) {
        tokenInfoMapper.deleteToken(token);
        //todo delete auth
        serviceAuthMapper.deleteAuthByToken(token);
        return true;
    }

    @Override
    public boolean checkToken(String token) {
        TokenInfo tokenInfo = tokenInfoMapper.getTokenInfo(token);
        if (tokenInfo == null) {
            return false;
        }
        if (!tokenInfo.isActive()) {
            return false;
        }
        Date nowDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tokenInfo.getRefreshTime());
        cal.add(Calendar.DATE, tokenInfo.getExpireTime());

        return nowDate.before(cal.getTime());
    }

    @Override
    public TokenInfo getTokenInfo(String token) {
        return tokenInfoMapper.getTokenInfo(token);
    }

    @Override
    public List<TokenInfo> getTokenInfos(String creator) {
        return tokenInfoMapper.getTokenInfoList(creator);
    }
}
