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

package com.upcwangying.cloud.samples.user.server.controller;

import com.upcwangying.cloud.samples.core.utils.ResultVOUtils;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import com.upcwangying.cloud.samples.user.common.entity.*;
import com.upcwangying.cloud.samples.user.server.entity.Role;
import com.upcwangying.cloud.samples.user.server.entity.RolePermission;
import com.upcwangying.cloud.samples.user.server.service.PermissionService;
import com.upcwangying.cloud.samples.user.server.service.RolePermissionService;
import com.upcwangying.cloud.samples.user.server.service.RoleService;
import com.upcwangying.cloud.samples.user.server.utils.BeanCreators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.upcwangying.cloud.samples.core.constant.Constants.DELETED_STATUS;
import static com.upcwangying.cloud.samples.core.enums.ResultEnum.DATA_EXISTED;
import static com.upcwangying.cloud.samples.core.enums.ResultEnum.DATA_NOT_EXIST;
import static com.upcwangying.cloud.samples.core.enums.ResultEnum.PARAM_ERROR;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping
    public ResultVO getRoleList() {
        List<Role> roles = roleService.findAll();
        List<RoleOutput> outputList = roles.stream().map(role -> {
            RoleOutput output = BeanCreators.createRoleOutput();
            BeanUtils.copyProperties(role, output);
            return output;
        }).collect(Collectors.toList());
        return ResultVOUtils.success(outputList);
    }

    /**
     * 根据角色Id, 获取角色详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\w{8}(?:-\\w{4}){3}-\\w{12}}")
    public ResultVO getRoleById(@PathVariable("id") String id) {
        Role role = roleService.findByRoleId(id);
        if (role == null) {
            log.error("RoleController: getRoleById(id={})", id);
            return ResultVOUtils.error(DATA_NOT_EXIST);
        }
        RoleOutput output = BeanCreators.createRoleOutput();
        BeanUtils.copyProperties(role, output);
        return ResultVOUtils.success(output);
    }

    /**
     * 根据角色Id, 获取角色下的权限
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\w{8}(?:-\\w{4}){3}-\\w{12}}/permissions")
    public ResultVO getPermissionByRoleId(@PathVariable("id") String id) {
        Role role = roleService.findByRoleId(id);
        if (role == null) {
            log.error("RoleController: getPermissionByRoleId(id={})", id);
            return ResultVOUtils.error(DATA_NOT_EXIST);
        }

        List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleId(id);
        if (CollectionUtils.isEmpty(rolePermissions)) {
            log.warn("RoleController: getPermissionByRoleId(id={})", id);
            return ResultVOUtils.success();
        }

        // 1. 获取角色下的权限id列表
        List<String> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // 2. 根据权限id列表，获取权限明细列表; 将权限明细列表, 转换为PermissionOutput列表
        List<PermissionOutput> permissionOutputs = permissionService.findAllByPermissionIds(permissionIds).stream()
                .map(permission -> {
                    PermissionOutput output = BeanCreators.createPermissionOutput();
                    BeanUtils.copyProperties(permission, output);
                    return output;
                }).collect(Collectors.toList());
        return ResultVOUtils.success(permissionOutputs);
    }

    /**
     * 根据角色Id, 更新角色下的权限
     *
     * @param id
     * @return
     */
    @PostMapping("/{id:\\w{8}(?:-\\w{4}){3}-\\w{12}}/permissions")
    public ResultVO updateRolePermission(@PathVariable("id") String id,
                                         @RequestBody List<String> permissionIds) {
        if (permissionIds == null || CollectionUtils.isEmpty(permissionIds)) {
            log.error("RoleController: getPermissionByRoleId(permissionIds={})", StringUtils.join(permissionIds, ","));
            return ResultVOUtils.error(PARAM_ERROR);
        }

        Role role = roleService.findByRoleId(id);
        if (role == null) {
            log.error("RoleController: getPermissionByRoleId(id={})", id);
            return ResultVOUtils.error(DATA_NOT_EXIST);
        }

        List<RolePermission> originRolePermissionList = rolePermissionService.findAllByRoleId(id).stream()
                .map(rolePermission -> {
                    rolePermission.setDelFlag(DELETED_STATUS);
                    return rolePermission;
                }).collect(Collectors.toList());
        rolePermissionService.saveAll(originRolePermissionList);

        List<RolePermission> rolePermissionList = permissionIds.stream().map(permissionId -> {
            RolePermission rolePermission = BeanCreators.createInitRolePermission();
            rolePermission.setRolePermissionId(UUID.randomUUID().toString());
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(permissionId);
            return rolePermission;
        }).collect(Collectors.toList());

        rolePermissionService.saveAll(rolePermissionList);
        return ResultVOUtils.success();
    }

    /**
     * 创建角色
     *
     * @param input
     * @return
     */
    @PostMapping
    public ResultVO createRole(@RequestBody @Validated RoleInput input, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResultVOUtils.error(PARAM_ERROR, errorsMap);
        }
        Role role = BeanCreators.createInitRole();
        BeanUtils.copyProperties(input, role);

        Role existedRole = roleService.findByRoleName(role.getRoleName());
        if (existedRole != null) {
            log.error("RoleController: createRole(role={})", input);
            return ResultVOUtils.error(DATA_EXISTED);
        }
        Role resultRole = roleService.saveOne(role);
        RoleOutput resultOutput = BeanCreators.createRoleOutput();
        BeanUtils.copyProperties(resultRole, resultOutput);
        return ResultVOUtils.success(resultOutput);
    }

    /**
     * 更新角色全部信息
     *
     * @param id    权限Id
     * @param input
     * @return
     */
    @PutMapping("/{id:\\w{8}(?:-\\w{4}){3}-\\w{12}}")
    public ResultVO updateRole(@PathVariable String id,
                               @RequestBody @Validated RoleInput input, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResultVOUtils.error(PARAM_ERROR, errorsMap);
        }

        Role role = roleService.findByRoleId(id);
        if (role == null) {
            log.error("RoleController: updateRole(id={}, input={})", id, input);
            return ResultVOUtils.error(DATA_NOT_EXIST);
        }

        BeanUtils.copyProperties(input, role);
        Role resultRole = roleService.saveOne(role);
        RoleOutput roleOutput = BeanCreators.createRoleOutput();
        BeanUtils.copyProperties(resultRole, roleOutput);
        return ResultVOUtils.success(roleOutput);
    }

    /**
     * 根据角色Id, 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id:\\w{8}(?:-\\w{4}){3}-\\w{12}}")
    public ResultVO deleteRoleById(@PathVariable("id") String id) {
        Role role = roleService.findByRoleId(id);
        if (role == null) {
            log.error("RoleController: deleteRoleById(id={})", id);
            return ResultVOUtils.error(DATA_NOT_EXIST);
        }
        role.setDelFlag(DELETED_STATUS);
        roleService.saveOne(role);
        return ResultVOUtils.success();
    }
}
