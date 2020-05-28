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

package com.upcwangying.cloud.samples.user.server.service.impl;

import com.upcwangying.cloud.samples.user.server.repository.UserRepository;
import com.upcwangying.cloud.samples.user.server.entity.User;
import com.upcwangying.cloud.samples.user.server.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.upcwangying.cloud.samples.core.constant.Constants.UNDELETED_STATUS;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询所有用户
     *
     * @return
     */
    @Override
    public List<User> findUserList() {
        return userRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameAndDelFlag(username, UNDELETED_STATUS);
    }

    /**
     * 根据用户Id, 获取用户详情
     *
     * @param userId
     * @return
     */
    @Override
    public User findByUserId(String userId) {
        return userRepository.findByUserIdAndDelFlag(userId, UNDELETED_STATUS);
    }

    /**
     * 创建或更新用户
     *
     * @param user
     * @return
     */
    @Override
    public User createOrUpdateUser(User user) {
        if (StringUtils.isBlank(user.getUserId())) {
            user.setUserId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

}
