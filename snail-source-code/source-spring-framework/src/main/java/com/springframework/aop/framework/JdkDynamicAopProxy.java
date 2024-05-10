package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;
import com.springframework.aop.MethodMatcher;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JdkDynamicAopProxy 是 Spring AOP 中的一个核心类，
 * 实现了 AopProxy 接口和 jdk 代理的 InvocationHandler 接口，用于创建使用 JDK 动态代理技术实现的 AOP 代理对象。
 * <p>
 * JDK 动态代理只能代理接口，因此 JdkDynamicAopProxy 只能为实现了接口的目标对象创建代理。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                advised.getTargetSource().getTargetClass(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 获取被代理的类
        Object targetObject = advised.getTargetSource().getTarget();
        // 2. 获取表达式匹配器
        MethodMatcher methodMatcher = advised.getMethodMatcher();
        // 3. 获取用户自己实现的方法拦截器
        MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
        // 4. 符合指定的切面规则的话，invoke 3.用户自己实现的方法拦截器
        if (methodMatcher.matches(method, targetObject.getClass())) {
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObject, method, args));
        }
        return method.invoke(targetObject, args);
    }

}
