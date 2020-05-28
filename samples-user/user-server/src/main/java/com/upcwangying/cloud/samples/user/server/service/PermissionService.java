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

import com.upcwangying.cloud.samples.user.server.entity.Permission;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public interface PermissionService {

    /**
     * 查询所有权限节点
     *
     * @return
     */
    List<Permission> findAll();

    /**
     * 查询权限子节点
     *
     * @param parentId
     * @return
     */
    List<Permission> findAllByParentId(String parentId);

    /**
     * 根据权限id, 查询权限节点详情
     *
     * @param permissionId
     * @return
     */
    Permission findByPermissionId(String permissionId);

    /**
     * 根据权限id集合, 查询权限详情列表
     *
     * @param permissionIds
     * @return
     */
    List<Permission> findAllByPermissionIds(List<String> permissionIds);

    /**
     * 根据权限name, 查询权限节点详情
     *
     * @param permissionName
     * @return
     */
    Permission findByPermissionName(String permissionName);

    /**
     * 保存权限节点
     *
     * @param permission
     * @return
     */
    Permission saveOne(Permission permission);

    /**
     * 保存权限节点
     *
     * @param permissions
     * @return
     */
    List<Permission> saveAll(List<Permission> permissions);
}
