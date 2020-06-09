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

package com.upcwangying.cloud.samples.order.server.ws.impl;

import com.upcwangying.cloud.samples.order.server.entity.OrderMain;
import com.upcwangying.cloud.samples.order.server.ws.IWbService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(name = "testService",
        targetNamespace = "http://service.webservicedemo.sxt.com",
        endpointInterface= "com.upcwangying.cloud.samples.order.server.ws.IWbService")

@Component
public class WebServiceImpl implements IWbService {

    @Override
    public String helloService(String name) {
        return "hello"+name;
    }

    @Override
    public List<OrderMain> getAllBean() {
        List<OrderMain> list = new ArrayList<>();
        OrderMain bean1 = new OrderMain();
        OrderMain bean2 = new OrderMain();
        list.add(bean1);
        list.add(bean2);
        return list;
    }

}
