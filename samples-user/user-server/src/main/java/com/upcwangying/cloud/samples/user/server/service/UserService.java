/*
 *
 * MIT License
 *
 * Copyright (c) 2019 cloud.upcwangying.com
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

package com.upcwangying.cloud.samples.user.server.service;


import com.upcwangying.cloud.samples.user.server.entity.User;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public interface UserService {

    /**
     * 查询所有用户
     *
     * @return
     */
    List<User> findUserList();

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return UserOutput
     */
    User findUserByUsername(String username);

    /**
     * 根据用户Id, 获取用户详情
     *
     * @param userId
     * @return UserOutput
     */
    User findByUserId(String userId);

    /**
     * 创建或更新用户
     *
     * @param user
     * @return UserOutput
     */
    User createOrUpdateUser(User user);

}
