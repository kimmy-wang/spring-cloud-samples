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

package com.upcwangying.cloud.samples.oss.web.config;

import com.upcwangying.cloud.samples.oss.core.config.OssConfiguration;
import com.upcwangying.cloud.samples.oss.server.service.IOssStoreService;
import com.upcwangying.cloud.samples.oss.server.service.impl.HdfsServiceImpl;
import com.upcwangying.cloud.samples.oss.server.service.impl.OssStoreServiceImpl;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author WANGY
 */
@Configuration
public class OssServerBeanConfiguration {

    /**
     * create hbase client connection.
     *
     * @return conn conn
     * @throws IOException ioe.
     */
    @Bean
    public Connection getConnection() throws IOException {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        OssConfiguration confUtil = OssConfiguration.getInstance();

        config.set("hbase.zookeeper.quorum", confUtil.getString("hbase.zookeeper.quorum"));
        config.set("hbase.zookeeper.property.clientPort",
                confUtil.getString("hbase.zookeeper.port"));
        config.set(HConstants.HBASE_RPC_TIMEOUT_KEY, "3600000");

        return ConnectionFactory.createConnection(config);
    }

    @Bean(name = "ossStoreService")
    public IOssStoreService getHosStore(@Autowired Connection connection) throws Exception {
        OssConfiguration confUtil = OssConfiguration.getInstance();
        String zkHosts = confUtil.getString("hbase.zookeeper.quorum");
        OssStoreServiceImpl store = new OssStoreServiceImpl(connection, new HdfsServiceImpl(),
                zkHosts);
        return store;
    }
}
