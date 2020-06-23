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

package com.upcwangying.cloud.samples.product.ws;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter {

    public static final String NAMESPACE = "http://upcwangying.com/ws/demo";
    public static final String TARGET_NAMESPACE = "http://upcwangying.com/webservice";

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "users")
    public DefaultWsdl11Definition defaultWsdl11Definition(@Qualifier("usersSchema") XsdSchema usersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("UserInput");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(TARGET_NAMESPACE);
        wsdl11Definition.setSchema(usersSchema);
        return wsdl11Definition;
    }

    @Bean(name = "demos")
    public DefaultWsdl11Definition demoWsdl11Definition(@Qualifier("demosSchema") XsdSchema demosSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("UserInput");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(TARGET_NAMESPACE);
        wsdl11Definition.setSchema(demosSchema);
        return wsdl11Definition;
    }

    @Bean(name = "usersSchema")
    public XsdSchema usersSchema() {
        return new SimpleXsdSchema(new ClassPathResource("user.xsd"));
    }

    @Bean(name = "demosSchema")
    public XsdSchema demosSchema() {
        return new SimpleXsdSchema(new ClassPathResource("demo.xsd"));
    }
}
