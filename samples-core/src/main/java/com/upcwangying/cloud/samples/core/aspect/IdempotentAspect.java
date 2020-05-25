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

package com.upcwangying.cloud.samples.core.aspect;

import com.upcwangying.cloud.samples.core.annotation.Idempotent;
import com.upcwangying.cloud.samples.core.service.IdempotentService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class IdempotentAspect {
    @Pointcut("@annotation(com.upcwangying.cloud.samples.core.annotation.Idempotent)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void permissionFilter(JoinPoint point) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        if (!method.isAccessible()) {
            return;
        }

        if (!method.isAnnotationPresent(Idempotent.class)) {
            return;
        }

        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation:
             annotations) {
            // 忽略 @GetMapping()
            if (annotation.getClass().equals(GetMapping.class)) {
                return;
            }
            // 忽略 @RequestMapping(method=RequestMethod.GET)
            if (annotation.getClass().equals(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) annotation;
                if (RequestMethod.GET.equals(requestMapping.method())) {
                    return;
                }
            }
        }

        Idempotent[] idempotencies = method.getAnnotationsByType(Idempotent.class);
        Idempotent idempotent = idempotencies[idempotencies.length - 1];
        Class<? extends IdempotentService> fallback = idempotent.fallback();
        try {
            IdempotentService service = fallback.newInstance();
            // true: 代表已是幂等性接口; 否则为false
            boolean idempotencyFlag = service.invoke();
            if (idempotencyFlag) {
                log.info("幂等性接口");
            } else {
                log.error("非幂等性接口");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
