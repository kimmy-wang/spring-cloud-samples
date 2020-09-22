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

package com.upcwangying.cloud.samples.order.web.service;

import com.upcwangying.cloud.samples.product.common.entity.ProductOutput;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import com.upcwangying.cloud.samples.order.common.entity.OrderDetailInput;
import com.upcwangying.cloud.samples.order.web.entity.OrderDetail;
import com.upcwangying.cloud.samples.order.web.entity.OrderMain;

import java.util.List;
import java.util.Map;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public interface OrderService {

    /**
     *
     * @return
     */
    List<OrderMain> getAllOrderMain();

    /**
     * 创建订单
     *
     * @return
     */
    OrderMain createOrder(List<OrderDetailInput> orderDetailInputList, Map<String, ProductOutput> productOutputMap);

    /**
     *
     * @param orderId
     * @return
     */
    List<OrderDetail> getAllOrderDetailByOrderId(String orderId);

    /**
     *
     * @param id
     * @return
     */
    OrderMain getOrderMainById(String id);

    /**
     *
     * @param orderMain
     * @return
     */
    OrderMain saveOne(OrderMain orderMain);

    /**
     *
     * @param productIdList
     * @return
     */
    ResultVO getProductListByProductIds(List<String> productIdList);
}
