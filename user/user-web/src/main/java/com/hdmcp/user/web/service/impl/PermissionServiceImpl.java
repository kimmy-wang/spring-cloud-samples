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

package com.hdmcp.user.web.service.impl;

import com.hdmcp.user.web.controller.PermissionController;
import com.hdmcp.user.web.repository.PermissionRepository;
import com.hdmcp.user.web.entity.Permission;
import com.hdmcp.user.web.service.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.hdmcp.core.constant.Constants.UNDELETED_STATUS;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 查询所有权限节点
     *
     * @return
     */
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    /**
     * 查询权限子节点
     *
     * @param parentId
     * @return
     */
    @Override
    public List<Permission> findAllByParentId(String parentId) {
        return permissionRepository.findAllByParentIdAndDelFlag(parentId, UNDELETED_STATUS);
    }

    /**
     * 根据权限id, 查询权限节点详情
     *
     * @param permissionId
     * @return
     */
    @Override
    public Permission findByPermissionId(String permissionId) {
        return permissionRepository.findByPermissionIdAndDelFlag(permissionId, UNDELETED_STATUS);
    }

    /**
     * 根据权限id集合, 查询权限详情列表
     *
     * @param permissionIds
     * @return
     */
    @Override
    public List<Permission> findAllByPermissionIds(List<String> permissionIds) {
        return permissionRepository.findAllByPermissionIdInAndDelFlag(permissionIds, UNDELETED_STATUS);
    }

    /**
     * 根据权限name, 查询权限节点详情
     *
     * @param permissionName
     * @return
     */
    @Override
    public Permission findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionNameAndDelFlag(permissionName, UNDELETED_STATUS);
    }

    /**
     * 保存权限节点
     *
     * @param permission
     * @return
     */
    public Permission saveOne(Permission permission) {
        if (StringUtils.isBlank(permission.getPermissionId())) {
            permission.setPermissionId(UUID.randomUUID().toString());
        }
        return permissionRepository.save(permission);
    }

    /**
     * 保存权限节点
     *
     * @param permissions
     * @return
     */
    @Override
    public List<Permission> saveAll(List<Permission> permissions) {
        return permissionRepository.saveAll(permissions);
    }

}
