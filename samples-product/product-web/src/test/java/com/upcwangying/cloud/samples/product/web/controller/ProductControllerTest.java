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

package com.upcwangying.cloud.samples.product.web.controller;

import com.upcwangying.cloud.samples.product.web.ProductServerApplicationTests;
import com.upcwangying.cloud.samples.product.web.service.ProductService;
import com.upcwangying.cloud.samples.product.web.utils.BeanCreators;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;

/**
 * @SpringBootTest 不能和  @WebMvcTest 同时使用
 * 如果使用MockMvc对象的话，需要另外加上@AutoConfigureMockMvc注解
 *
 * @author WANGY
 */
@WebMvcTest
public class ProductControllerTest extends ProductServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /**
     * <a href="https://github.com/jayway/JsonPath">JsonPath</a>
     * @throws Exception
     */
    @Test
    public void getAllProduct() throws Exception {
        given(this.productService.getAllProduct())
                .willReturn(Arrays.asList(BeanCreators.createProduct(), BeanCreators.createProduct()));
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg", Matchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)));
    }

    @Test
    public void createProduct() {

    }

    @Test
    public void getProductByProductId() {
    }

    @Test
    public void updateProduct2() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void deleteProduct() {
    }
}
