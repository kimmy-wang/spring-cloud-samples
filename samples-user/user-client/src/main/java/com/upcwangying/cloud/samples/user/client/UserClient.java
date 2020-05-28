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

package com.upcwangying.cloud.samples.user.client;

import com.upcwangying.cloud.samples.user.common.entity.PermissionOutput;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 此接口供其他服务调用
 *
 * @author WANGY
 */
@FeignClient(name = "user", fallback = UserClient.UserClientFallback.class)
public interface UserClient {

    /**
     * 根据用户名查询用户所有权限
     *
     * @return
     */
    @GetMapping("/users/{id}/permissions")
    @ResponseBody
    ResultVO<List<PermissionOutput>> getUserPermissionByUserId(@PathVariable("id") String id);

    @Component
    static class UserClientFallback implements UserClient {

        /**
         * 根据用户名查询用户所有权限
         *
         * @param id
         * @return
         */
        @Override
        public ResultVO<List<PermissionOutput>> getUserPermissionByUserId(String id) {
            return null;
        }
    }
}
