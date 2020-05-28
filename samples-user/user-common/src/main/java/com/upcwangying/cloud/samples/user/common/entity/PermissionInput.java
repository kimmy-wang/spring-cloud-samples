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

package com.upcwangying.cloud.samples.user.common.entity;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 权限表
 *
 * @author WANGY
 */
public class PermissionInput implements Serializable {
    private static final long serialVersionUID = 218316083242080362L;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * 权限名
     */
    @NotBlank(message = "权限名字不允许为空")
    private String permissionName;

    /**
     * 权限点
     */
    private String permissionPath;

    /**
     * 请求方法
     */
    @NotBlank(message = "请求方法不允许为空")
    private String requestMethod;

    /**
     * 权限描述
     */
    private String permissionDesc;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionPath() {
        return permissionPath;
    }

    public void setPermissionPath(String permissionPath) {
        this.permissionPath = permissionPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getPermissionDesc() {
        return permissionDesc;
    }

    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }

    @Override
    public String toString() {
        return "PermissionInput{" +
                "parentId='" + parentId + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", permissionDesc='" + permissionDesc + '\'' +
                '}';
    }
}
