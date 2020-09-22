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

package com.upcwangying.cloud.samples.user.web.assembler;

import com.upcwangying.cloud.samples.user.common.entity.UserInput;
import com.upcwangying.cloud.samples.user.common.entity.UserOutput;
import com.upcwangying.cloud.samples.user.web.controller.UserController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Component
public class UserOutputResourceAssembler implements ResourceAssembler<UserOutput, Resource<UserOutput>> {

    @Override
    public Resource<UserOutput> toResource(UserOutput userOutput) {
        return new Resource<>(
                userOutput,
                linkTo(methodOn(UserController.class).getUserById(userOutput.getUserId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserList()).withRel("users"),
                linkTo(methodOn(UserController.class).deleteUserById(userOutput.getUserId())).withRel("delete_user"),
                linkTo(methodOn(UserController.class).getUserPermissionByUserId(userOutput.getUserId())).withRel("user_permissions"),
                linkTo(methodOn(UserController.class).getUserTokenByUsername(userOutput.getUsername(), "password")).withRel("user_token"),
                linkTo(methodOn(UserController.class).refreshToken(null)).withRel("user_refresh_token"),
                linkTo(methodOn(UserController.class).createUser(new UserInput(), null)).withRel("create_user")
        );
    }
}
