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

package com.hdmcp.core.aspect;

import com.hdmcp.core.annotation.Idempotent;
import com.hdmcp.core.service.IdempotentService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class IdempotentAspect {
    private Logger log = LoggerFactory.getLogger(IdempotentAspect.class);
    private static final String TAG = IdempotentAspect.class.getName();

    @Pointcut("@annotation(com.hdmcp.core.annotation.Idempotent)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void permissionFilter(JoinPoint point) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes()).getResponse();

        // 非public方法
        if (!method.isAccessible()) {
            return;
        }

        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation :
                annotations) {
            // 忽略 @GetMapping() || @RequestMapping(method=RequestMethod.GET)
            if (annotation.getClass().equals(GetMapping.class)
                    || (annotation.getClass().equals(RequestMapping.class)
                    && RequestMethod.GET.equals(((RequestMapping) annotation).method()))) {
                log.warn(TAG, "GET");
                return;
            } else if (idempotent(annotation)) {
                // PUT, PATCH, POST, DELETE
                if (!method.isAnnotationPresent(Idempotent.class)) {
                    log.error(TAG, "方法未使用幂等性注解", method.getName());
                }
            } else {
                // todo HEAD, OPTIONS, TRACE
                log.warn(TAG, "HEAD, OPTIONS, TRACE");
            }
        }

        Idempotent[] idempotence = method.getAnnotationsByType(Idempotent.class);
        Idempotent idempotent = idempotence[idempotence.length - 1];
        Class<? extends IdempotentService> fallback = idempotent.fallback();
        try {
            IdempotentService service = fallback.newInstance();
            // true: 代表已是幂等性接口; 否则为false
            boolean idempotentFlag = service.isIdempotent();
            if (idempotentFlag) {
                log.info("幂等性接口");
            } else {
                log.error("非幂等性接口");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean idempotent(Annotation annotation) {
        List<RequestMethod> idempotenceMethods = Arrays.asList(RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE);
        if (RequestMapping.class.equals(annotation.getClass())) {
            RequestMethod[] methods = ((RequestMapping) annotation).method();
            for (RequestMethod method: methods) {
                if (idempotenceMethods.contains(method)) return true;
            }
            return false;
        }

        List<Class> idempotenceMaps = Arrays.asList(PostMapping.class, PutMapping.class, PatchMapping.class, DeleteMapping.class);
        return idempotenceMaps.contains(annotation.getClass());
    }

}
