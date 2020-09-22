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

package com.hdmcp.order.web.utils;

import com.hdmcp.order.web.entity.OrderDetail;
import com.hdmcp.order.web.entity.OrderMain;

import static com.hdmcp.core.constant.Constants.UNDELETED_STATUS;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public class BeanCreators extends com.hdmcp.order.common.utils.BeanCreators {
    public static OrderMain createOrderMain() {
        OrderMain orderMain = new OrderMain();
        orderMain.setDelFlag(UNDELETED_STATUS);
        return orderMain;
    }

    public static OrderDetail createOrderDetail() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDelFlag(UNDELETED_STATUS);
        return orderDetail;
    }
}
