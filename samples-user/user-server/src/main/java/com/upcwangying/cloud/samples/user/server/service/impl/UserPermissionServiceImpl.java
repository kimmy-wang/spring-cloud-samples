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

import com.upcwangying.cloud.samples.user.server.repository.UserPermissionRepository;
import com.upcwangying.cloud.samples.user.server.entity.UserPermission;
import com.upcwangying.cloud.samples.user.server.service.UserPermissionService;
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
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    /**
     * 查询用户权限关系列表
     *
     * @return
     */
    @Override
    public List<UserPermission> findAll() {
        return userPermissionRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    /**
     * 根据用户权限关系id, 查询用户权限关系详情
     *
     * @param userPermissionId 用户权限id
     * @return
     */
    @Override
    public UserPermission findByUserPermissionId(String userPermissionId) {
        return userPermissionRepository.findByUserPermissionIdAndDelFlag(userPermissionId, UNDELETED_STATUS);
    }

    /**
     * 根据用户id, 查询该用户对应的角色权限关系
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserPermission> findAllByUserId(String userId) {
        return userPermissionRepository.findAllByUserIdAndDelFlag(userId, UNDELETED_STATUS);
    }

    /**
     * 保存用户权限关系
     *
     * @param userPermission
     * @return
     */
    @Override
    public UserPermission saveOne(UserPermission userPermission) {
        if (StringUtils.isBlank(userPermission.getUserPermissionId())) {
            userPermission.setUserPermissionId(UUID.randomUUID().toString());
        }
        return userPermissionRepository.save(userPermission);
    }

    /**
     * 保存用户权限关系
     *
     * @param userPermissionList
     * @return
     */
    @Override
    public List<UserPermission> saveAll(List<UserPermission> userPermissionList) {
        return userPermissionRepository.saveAll(userPermissionList);
    }
}
