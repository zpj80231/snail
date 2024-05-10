package com.springframework.cyclicDependence;

import com.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2023/9/19
 */
public class BAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("BAdvice before method: " + method);
    }

}
