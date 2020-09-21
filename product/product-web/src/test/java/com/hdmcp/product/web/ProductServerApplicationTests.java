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

package com.hdmcp.product.web;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * <a href="https://docs.spring.io/spring-boot/docs/2.0.9.RELEASE/reference/htmlsingle/#boot-features-testing">Testing<a/>
 *
 * webEnvironment:
 *  WebEnvironment.MOCK—提供一个Mock的Servlet环境，内置的Servlet容器并没有真实的启动，主要搭配使用@AutoConfigureMockMvc
 *  WebEnvironment.RANDOM_PORT — 提供一个真实的Servlet环境，也就是说会启动内置容器，然后使用的是随机端口
 *  WebEnvironment.DEFINED_PORT — 这个配置也是提供一个真实的Servlet环境，使用的默认的端口，如果没有配置就是8080
 *  WebEnvironment.NONE — 这是个神奇的配置，跟Mock一样也不提供真实的Servlet环境。
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test") //在测试类上面指定profiles，可以改变当前spring 的profile，来达到多环境的测试
public class ProductServerApplicationTests {
    private static Logger log = LoggerFactory.getLogger(ProductServerApplicationTests.class);

    /**
     * 在单个测试类所有测试方法前执行一次，一般在其中写上整体初始化的代码
     */
    @BeforeClass
    public static void beforeClass() {
        log.info("~~~~~~~~所有测试方法执行前调用~~~~~~~~");
    }

    /**
     * 在单个测试类所有测试方法后执行一次，一般在其中写上销毁和释放资源的代码
     */
    @AfterClass
    public static void afterClass() {
        log.info("~~~~~~~~所有测试方法执行后调用~~~~~~~~");
    }

    /**
     * 在每个测试方法前执行，
     * 一般用来初始化方法（比如我们在测试别的方法时，类中与其他测试方法共享的值已经被改变，为了保证测试结果的有效性，我们会在@Before注解的方法中重置数据）
     */
    @Before
    public void init() {
        log.info("~~~~~~~~单个测试方法执行前调用~~~~~~~~");
    }

    /**
     * 在每个测试方法后执行，在方法执行完成后要做的事情
     */
    @After
    public void after() {
        log.info("~~~~~~~~单个测试方法执行后调用~~~~~~~~");
    }

}
