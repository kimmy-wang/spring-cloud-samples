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

package com.hdmcp.product.web.repository;

import com.hdmcp.product.web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品服务持久层
 *
 * @author WANGY
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * 通过所有商品
     *
     * @param delFlag 删除标识
     * @return
     */
    List<Product> findAllByDelFlag(char delFlag);

    /**
     * 通过商品ID，查找商品
     *
     * @param productId 商品ID
     * @param delFlag 删除标识
     * @return
     */
    Product findByProductIdAndDelFlag(String productId, char delFlag);

    /**
     * 通过商品ID，查找商品
     *
     * @param productIdList 商品ID列表
     * @param delFlag 删除标识
     * @return
     */
    List<Product> findAllByProductIdInAndDelFlag(List<String> productIdList, char delFlag);
}
