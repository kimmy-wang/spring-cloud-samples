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

package com.hdmcp.user.web.service;

import com.hdmcp.user.web.entity.RolePermission;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public interface RolePermissionService {

    /**
     * 查询角色权限关系列表
     *
     * @return
     */
    List<RolePermission> findAll();

    /**
     * 根据角色权限关系id, 查询角色权限关系详情
     *
     * @param rolePermissionId 角色id
     * @return
     */
    RolePermission findByRolePermissionId(String rolePermissionId);

    /**
     * 根据角色id, 查询该角色对应的角色权限关系
     *
     * @param roleId 角色id
     * @return
     */
    List<RolePermission> findAllByRoleId(String roleId);

    /**
     * 保存角色权限关系
     *
     * @param rolePermission
     * @return
     */
    RolePermission saveOne(RolePermission rolePermission);

    /**
     * 保存角色权限关系
     *
     * @param rolePermissionList
     * @return
     */
    List<RolePermission> saveAll(List<RolePermission> rolePermissionList);

    /**
     * 根据给定的角色id数组, 查询这些角色下的权限列表
     *
     * @param roleIds 角色id数组
     * @return
     */
    List<RolePermission> findAllByRoleIds(List<String> roleIds);

}
