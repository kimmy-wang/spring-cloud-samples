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

import com.upcwangying.cloud.samples.user.server.entity.UserPermission;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 * @date 2019/4/30 14:49
 */
public interface UserPermissionService {

    /**
     * 查询用户权限关系列表
     *
     * @return
     */
    List<UserPermission> findAll();

    /**
     * 根据用户权限关系id, 查询用户权限关系详情
     *
     * @param userPermissionId 用户权限id
     * @return
     */
    UserPermission findByUserPermissionId(String userPermissionId);

    /**
     * 根据用户id, 查询该用户对应的角色权限关系
     *
     * @param userId 用户id
     * @return
     */
    List<UserPermission> findAllByUserId(String userId);

    /**
     * 保存用户权限关系
     *
     * @param userPermission
     * @return
     */
    UserPermission saveOne(UserPermission userPermission);

    /**
     * 保存用户权限关系
     *
     * @param userPermissionList
     * @return
     */
    List<UserPermission> saveAll(List<UserPermission> userPermissionList);

}
