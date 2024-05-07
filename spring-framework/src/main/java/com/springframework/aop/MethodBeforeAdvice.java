package com.springframework.aop;

import java.lang.reflect.Method;

/**
 * MethodBeforeAdvice 用于在目标方法执行之前执行一些额外的逻辑操作。
 * 通过实现 MethodBeforeAdvice 接口，开发人员可以在目标方法执行之前插入自定义的行为，比如日志记录、权限验证、事务管理等。
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * aop 方法前置操作
     *
     * @param method 目标方法
     * @param args   目标方法参数
     * @param target 目标对象，可能是空
     * @throws Throwable throwable
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
