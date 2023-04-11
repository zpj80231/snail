package com.snail.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * ReflectiveMethodInvocation 是 Spring AOP 中实现 MethodInvocation 接口的一个具体类，用于封装方法调用信息并提供一些便捷的操作方法。
 * 在 AOP 操作中，ReflectiveMethodInvocation 负责将目标对象的方法调用转发到代理对象，并提供了运行时切面通知所需的 Method 对象、参数，以及目标对象等信息。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    /**
     * 目标
     */
    protected final Object target;
    /**
     * 方法
     */
    protected final Method method;
    /**
     * 参数
     */
    protected final Object[] arguments;

    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}
