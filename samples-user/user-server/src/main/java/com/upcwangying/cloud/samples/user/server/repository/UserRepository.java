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

package com.upcwangying.cloud.samples.user.server.repository;

import com.upcwangying.cloud.samples.user.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 * @date 2019/4/25 10:41
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 查询所有用户
     *
     * @param delFlag
     * @return
     */
    List<User> findAllByDelFlag(char delFlag);

    /**
     * 根据用户名和密码查找用户
     *
     * @param username
     * @param delFlag
     * @return
     */
    User findByUsernameAndDelFlag(String username, char delFlag);

    /**
     * 根据用户Id, 获取用户详情
     *
     * @param userId
     * @param delFlag
     * @return
     */
    User findByUserIdAndDelFlag(String userId, char delFlag);

}
