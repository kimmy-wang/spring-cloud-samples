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

package com.hdmcp.product.web.service.impl;

import com.hdmcp.core.constant.Constants;
import com.hdmcp.product.web.repository.ProductRepository;
import com.hdmcp.product.web.entity.Product;
import com.hdmcp.product.web.service.ProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 查询所有商品
     * @return
     */
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAllByDelFlag(Constants.UNDELETED_STATUS);
    }

    /**
     * 查询商品明细
     * @return
     */
    @Override
    public Product getProductById(String productId) {
        return productRepository.findByProductIdAndDelFlag(productId, Constants.UNDELETED_STATUS);
    }

    /**
     * 查询商品明细
     * @return
     */
    @Override
    public List<Product> getProductById(List<String> productIdList) {
        return productRepository.findAllByProductIdInAndDelFlag(productIdList, Constants.UNDELETED_STATUS);
    }

    /**
     * 新增、更新、删除商品
     * @return
     */
    @Override
    public Product saveOne(Product product) {
        if (StringUtils.isBlank(product.getProductId())) {
            product.setProductId(UUID.randomUUID().toString());
        }
        return productRepository.save(product);
    }

    /**
     * 批量新增、更新、删除商品
     * @return
     */
    @Override
    public List<Product> saveAll(List<Product> productList) {
        productList = productList.stream().map(product -> {
            if (StringUtils.isBlank(product.getProductId())) {
                product.setProductId(UUID.randomUUID().toString());
            }
            return product;
        }).collect(Collectors.toList());
        return productRepository.saveAll(productList);
    }
}
