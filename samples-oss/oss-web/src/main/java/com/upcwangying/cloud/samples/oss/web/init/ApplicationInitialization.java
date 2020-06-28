/*
 *
 * MIT License
 *
 * Copyright (c) 2020 cloud.upcwangying.com
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.upcwangying.cloud.samples.oss.web.init;

import com.upcwangying.cloud.samples.oss.core.entity.UserInfo;
import com.upcwangying.cloud.samples.oss.core.enums.SystemRole;
import com.upcwangying.cloud.samples.oss.core.service.IUserService;
import com.upcwangying.cloud.samples.oss.core.utils.CoreUtil;
import com.upcwangying.cloud.samples.oss.server.service.IOssStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author WANGY
 */
@Component
public class ApplicationInitialization implements ApplicationRunner {

    @Autowired
    @Qualifier("oosStoreService")
    IOssStoreService oosStoreService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserInfo userInfo = userService.getUserByName(CoreUtil.SYSTEM_USER);
        if (userInfo == null) {
            userInfo = new UserInfo(CoreUtil.SYSTEM_USER, "superadmin", "this is superadmin", SystemRole.SUPER_ADMIN);
            userService.addUser(userInfo);
        }
        oosStoreService.createSeqTable();
    }
}
