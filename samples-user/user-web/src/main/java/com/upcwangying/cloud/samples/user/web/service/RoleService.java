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

package com.upcwangying.cloud.samples.user.web.service;

import com.upcwangying.cloud.samples.user.web.entity.Role;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public interface RoleService {

    /**
     * 查询所有角色
     *
     * @return
     */
    List<Role> findAll();

    /**
     * 根据角色id, 查询角色详情
     *
     * @param roleId 角色id
     * @return
     */
    Role findByRoleId(String roleId);

    /**
     * 根据角色name, 查询角色详情
     *
     * @param roleName 角色name
     * @return
     */
    Role findByRoleName(String roleName);

    /**
     * 保存角色
     *
     * @param role
     * @return
     */
    Role saveOne(Role role);

}
