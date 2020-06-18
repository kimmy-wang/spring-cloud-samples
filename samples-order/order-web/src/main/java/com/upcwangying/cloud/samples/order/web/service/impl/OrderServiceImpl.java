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

package com.upcwangying.cloud.samples.order.web.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.upcwangying.cloud.samples.order.web.repository.OrderDetailRepository;
import com.upcwangying.cloud.samples.order.web.repository.OrderMainRepository;
import com.upcwangying.cloud.samples.product.feign.ProductClient;
import com.upcwangying.cloud.samples.product.common.entity.ProductInput;
import com.upcwangying.cloud.samples.product.common.entity.ProductOutput;
import com.upcwangying.cloud.samples.product.common.utils.BeanCreators;
import com.upcwangying.cloud.samples.core.exception.FallbackException;
import com.upcwangying.cloud.samples.core.utils.ResultVOUtils;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import com.upcwangying.cloud.samples.order.common.entity.OrderDetailInput;
import com.upcwangying.cloud.samples.order.web.entity.OrderDetail;
import com.upcwangying.cloud.samples.order.web.entity.OrderMain;
import com.upcwangying.cloud.samples.order.web.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.upcwangying.cloud.samples.core.constant.Constants.DELETED_STATUS;
import static com.upcwangying.cloud.samples.core.constant.Constants.UNDELETED_STATUS;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMainRepository orderMainRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public List<OrderMain> getAllOrderMain() {
        return orderMainRepository.findAllByDelFlag(UNDELETED_STATUS);
    }

    @Override
    @HystrixCommand(fallbackMethod = "createOrderFallbackMethod")
    public OrderMain createOrder(List<OrderDetailInput> orderDetailInputList,
                                 Map<String, ProductOutput> productOutputMap) {
        // 1.计算orderMain总价
        OrderMain orderMain = com.upcwangying.cloud.samples.order.web.utils.BeanCreators.createOrderMain();
        String orderId = UUID.randomUUID().toString();
        orderMain.setOrderId(orderId);
        orderMain.setBuyer("admin");
        BigDecimal orderPrice = orderDetailInputList.stream()
                .map(
                        orderDetailInput ->
                                productOutputMap.get(orderDetailInput.getProductId()).getPrice().multiply(BigDecimal.valueOf(orderDetailInput.getProductQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderMain.setOrderPrice(orderPrice);
        List<OrderDetail> orderDetailList = orderDetailInputList.stream()
                .map(orderDetailInput -> {
                    OrderDetail orderDetail = com.upcwangying.cloud.samples.order.web.utils.BeanCreators.createOrderDetail();
                    ProductOutput productOutput = productOutputMap.get(orderDetailInput.getProductId());
                    BeanUtils.copyProperties(productOutput, orderDetail);
                    orderDetail.setOrderDetailId(UUID.randomUUID().toString());
                    orderDetail.setOrderId(orderId);
                    orderDetail.setProductPrice(productOutput.getPrice());
                    orderDetail.setProductQuantity(orderDetailInput.getProductQuantity());
                    return orderDetail;
                })
                .collect(Collectors.toList());

        List<ProductInput> productInputList = orderDetailInputList.stream()
                .map(orderDetailInput -> {
                    ProductInput productInput = BeanCreators.createProductInput();
                    ProductOutput productOutput = productOutputMap.get(orderDetailInput.getProductId());
                    BeanUtils.copyProperties(productOutput, productInput);
                    productInput.setStock(productOutput.getStock() - orderDetailInput.getProductQuantity());
                    return productInput;
                })
                .collect(Collectors.toList());

        orderMainRepository.save(orderMain);
        orderDetailRepository.saveAll(orderDetailList);
        ResultVO resultVO = productClient.updateProduct(productInputList);
        if (resultVO == null) {
            log.error("order: params=[orderDetailInputList:{}], error=[msg:{}]",
                    orderDetailInputList, "ProductClient::updateProduct调用异常, 触发降级.");
            throw new FallbackException();
        }
        return orderMain;
    }

    @Override
    public List<OrderDetail> getAllOrderDetailByOrderId(String orderId) {
        return orderDetailRepository.findAllByOrderIdAndDelFlag(orderId, UNDELETED_STATUS);
    }

    @Override
    public OrderMain getOrderMainById(String id) {
        return orderMainRepository.findByOrderIdAndDelFlag(id, UNDELETED_STATUS);
    }

    @Override
    public OrderMain saveOne(OrderMain orderMain) {
        List<OrderDetail> orderDetailList = getAllOrderDetailByOrderId(orderMain.getOrderId());
        orderDetailList = orderDetailList.stream()
                .map(orderDetail -> {
                    orderDetail.setDelFlag(DELETED_STATUS);
                    return orderDetail;
                })
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(orderDetailList)) {
            orderDetailRepository.saveAll(orderDetailList);
        }
        return orderMainRepository.save(orderMain);
    }

    @Override
    @HystrixCommand(fallbackMethod = "getProductListByProductIdsFallbackMethod")
    public ResultVO getProductListByProductIds(List<String> productIdList) {
        ResultVO resultVO = productClient.getProductListByProductIds(productIdList);
        if (resultVO == null) {
            log.error("order: params=[productIdList:{}], error=[msg:{}]",
                    productIdList, "ProductClient::getProductListByProductIds调用异常, 触发降级.");
            throw new FallbackException();
        }
        return resultVO;
    }

    public ResultVO getProductListByProductIdsFallbackMethod(List<String> productIdList) {
        return ResultVOUtils.error(String.format("order: params=[productIdList:%s], error=[msg:%s]",
                String.join(",", productIdList), "触发降级, 执行OrderServiceImpl::getProductListByProductIdsFallbackMethod逻辑."));
    }

    public OrderMain createOrderFallbackMethod(List<OrderDetailInput> orderDetailInputList,
                                               Map<String, ProductOutput> productOutputMap) {
        return null;
    }

}
