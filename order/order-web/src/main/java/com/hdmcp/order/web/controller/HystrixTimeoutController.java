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

package com.hdmcp.order.web.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.hdmcp.core.vo.ResultVO;
import com.hdmcp.order.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 超时配置
 *
 * @author WANGY
 */
@RestController
@RequestMapping("timeout")
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixTimeoutController {

    @Autowired
    private OrderService orderService;


    /**
     * 属性查看{@link com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager#EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS},
     * 更多设置请查看{@link com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager}
     * 默认设置请查看{@link com.netflix.hystrix.HystrixCommandProperties}
     * @return
     */
    @GetMapping("test")
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
//    })
    @HystrixCommand(commandKey = "HystrixTimeoutController_test")
    public String test() {
//        orderService.getAllOrder();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("http://127.0.0.1:9092/product", ResultVO.class);

        return "success";
    }

    /**
     * Hystrix与Feign集成
     * 1、启用配置 feign.hystrix.enabled: true
     * 2、在ProductClient中定义fallback
     * @return
     */
    @GetMapping("test2")
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
//    })
    @HystrixCommand(commandKey = "HystrixTimeoutController_test")
    public String test2() {
        orderService.getAllOrderMain();

        return "success";
    }

    private String defaultFallback() {
        return "默认提示: 太拥挤了, 请稍后重试~~~";
    }
}
