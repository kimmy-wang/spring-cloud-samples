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

package com.upcwangying.cloud.samples.user.web.repository;

import com.upcwangying.cloud.samples.user.web.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {

    /**
     * 查询所有权限节点
     *
     * @param delFlag
     * @return
     */
    List<RolePermission> findAllByDelFlag(char delFlag);

    /**
     * 根据角色权限关系id, 查询角色权限关系详情
     *
     * @param rolePermissionId 角色id
     * @param delFlag
     * @return
     */
    RolePermission findByRolePermissionIdAndDelFlag(String rolePermissionId, char delFlag);

    /**
     * 根据角色id, 查询该角色下的权限列表
     *
     * @param roleId 角色id
     * @param delFlag
     * @return
     */
    List<RolePermission> findAllByRoleIdAndDelFlag(String roleId, char delFlag);

    /**
     * 根据给定的角色id数组, 查询这些角色下的权限列表
     *
     * @param roleIds 角色id数组
     * @param delFlag
     * @return
     */
    List<RolePermission> findAllByRoleIdInAndDelFlag(List<String> roleIds, char delFlag);

}
