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

package com.upcwangying.cloud.samples.oss.core.service;

import com.upcwangying.cloud.samples.oss.core.entity.ServiceAuth;
import com.upcwangying.cloud.samples.oss.core.entity.TokenInfo;

import java.util.List;

/**
 * @author WANGY
 */
public interface IAuthService {
    public boolean addAuth(ServiceAuth auth);

    public boolean deleteAuth(String bucketName, String token);

    public boolean deleteAuthByBucket(String bucketName);

    public boolean deleteAuthByToken(String token);

    public ServiceAuth getServiceAuth(String bucketName, String token);

    public boolean addToken(TokenInfo tokenInfo);

    public boolean updateToken(String token, int expireTime, boolean isActive);

    public boolean refreshToken(String token);

    public boolean deleteToken(String token);

    public boolean checkToken(String token);

    public TokenInfo getTokenInfo(String token);

    public List<TokenInfo> getTokenInfos(String creator);
}
