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

import com.upcwangying.cloud.samples.user.server.repository.RoleRepository;
import com.upcwangying.cloud.samples.user.server.entity.Role;
import com.upcwangying.cloud.samples.user.server.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 查询所有权限节点
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    /**
     * 根据权限id, 查询权限节点详情
     *
     * @param roleId
     * @return
     */
    @Override
    public Role findByRoleId(String roleId) {
        return roleRepository.findByRoleIdAndDelFlag(roleId, UNDELETED_STATUS);
    }

    /**
     * 根据权限name, 查询权限节点详情
     *
     * @param roleName
     * @return
     */
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleNameAndDelFlag(roleName, UNDELETED_STATUS);
    }

    /**
     * 保存权限节点
     *
     * @param role
     * @return
     */
    @Override
    public Role saveOne(Role role) {
        if (StringUtils.isBlank(role.getRoleId())) {
            role.setRoleId(UUID.randomUUID().toString());
        }
        return roleRepository.save(role);
    }

}
