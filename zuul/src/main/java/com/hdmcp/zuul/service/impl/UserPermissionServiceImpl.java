/*
 *
 * MIT License
 *
 * Copyright (c) 2019 hdmcp.com
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.hdmcp.zuul.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.hdmcp.core.exception.FallbackException;
import com.hdmcp.core.utils.ResultVOUtils;
import com.hdmcp.core.vo.ResultVO;
import com.hdmcp.user.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upcwangying.cloud.see.auth.service.UserPermissionService;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
@Slf4j
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserClient userClient;

    /**
     * 根据用户名获取用户所有权限
     *
     * @param userId
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserPermissionByUserIdFallbackMethod")
    @Override
    public ResultVO getUserPermissionByUserId(String userId) {
        ResultVO resultVO = userClient.getUserPermissionByUserId(userId);
        if (resultVO == null) {
            throw new FallbackException();
        }
        return resultVO;
    }

    public ResultVO getUserPermissionByUserIdFallbackMethod(String username) {
        // todo
        return ResultVOUtils.error("getUserPermissionByUserIdFallbackMethod");
    }
}
