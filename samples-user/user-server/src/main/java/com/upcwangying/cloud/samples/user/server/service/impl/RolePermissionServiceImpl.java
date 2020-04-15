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

import com.upcwangying.cloud.samples.user.server.repository.RolePermissionRepository;
import com.upcwangying.cloud.samples.user.server.entity.RolePermission;
import com.upcwangying.cloud.samples.user.server.service.RolePermissionService;
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
 * @date 2019/5/7 14:01
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    /**
     * 查询角色权限关系列表
     *
     * @return
     */
    @Override
    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    /**
     * 根据角色权限关系id, 查询角色权限关系详情
     *
     * @param rolePermissionId 角色id
     * @return
     */
    @Override
    public RolePermission findByRolePermissionId(String rolePermissionId) {
        return rolePermissionRepository.findByRolePermissionIdAndDelFlag(rolePermissionId, UNDELETED_STATUS);
    }

    /**
     * 根据角色id, 查询该角色对应的角色权限关系
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<RolePermission> findAllByRoleId(String roleId) {
        return rolePermissionRepository.findAllByRoleIdAndDelFlag(roleId, UNDELETED_STATUS);
    }

    /**
     * 保存角色权限关系
     *
     * @param rolePermission
     * @return
     */
    @Override
    public RolePermission saveOne(RolePermission rolePermission) {
        if (StringUtils.isBlank(rolePermission.getRolePermissionId())) {
            rolePermission.setRolePermissionId(UUID.randomUUID().toString());
        }
        return rolePermissionRepository.save(rolePermission);
    }

    /**
     * 保存角色权限关系
     *
     * @param rolePermissionList
     * @return
     */
    @Override
    public List<RolePermission> saveAll(List<RolePermission> rolePermissionList) {
        return rolePermissionRepository.saveAll(rolePermissionList);
    }

    /**
     * 根据给定的角色id数组, 查询这些角色下的权限列表
     *
     * @param roleIds 角色id数组
     * @return
     */
    @Override
    public List<RolePermission> findAllByRoleIds(List<String> roleIds) {
        return rolePermissionRepository.findAllByRoleIdInAndDelFlag(roleIds, UNDELETED_STATUS);
    }
}
