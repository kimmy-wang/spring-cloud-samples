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

package com.upcwangying.cloud.samples.product.server.service.impl;

import com.upcwangying.cloud.samples.product.server.ProductServerApplicationTests;
import com.upcwangying.cloud.samples.product.server.entity.Product;
import com.upcwangying.cloud.samples.product.server.repository.ProductRepository;
import com.upcwangying.cloud.samples.product.server.service.ProductService;
import com.upcwangying.cloud.samples.product.server.utils.BeanCreators;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * Created by WANGY
 *
 * @author WANGY
 * @date 2019/4/22 13:41
 */
@SpringBootTest
@Slf4j
public class ProductServiceImplTest extends ProductServerApplicationTests {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    /**
     * @Test(timeout = 1000):测试方法执行超过1000毫秒后算超时，测试将失败
     * @Test(expected = Exception.class):测试方法期望得到的异常类，如果方法执行没有抛出指定的异常，则测试失败
     * @Ignore("not ready yet") 执行测试时将忽略掉此方法，如果用于修饰类，则忽略整个类
     */
    @Test
    public void getAllProduct() {
        given(this.productRepository.findAllByDelFlag('0'))
                .willReturn(Arrays.asList(BeanCreators.createProduct(), BeanCreators.createProduct()));
        List<Product> productList = productService.getAllProduct();
        Assert.assertEquals("获取所有商品列表", 2, productList.size());
    }

    @Test
    public void getProductById() {

    }

    @Test
    public void insertOrUpdateProduct() {
    }
}
