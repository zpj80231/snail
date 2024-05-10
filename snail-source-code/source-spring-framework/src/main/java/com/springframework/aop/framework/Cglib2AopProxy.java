package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;
import com.springframework.aop.MethodMatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Cglib2AopProxy 是 Spring AOP 中基于 CGLIB 技术实现的代理对象创建类，实现了 AopProxy 接口。
 * 与 JDK 动态代理不同，CGLIB 可以为没有实现接口的类生成代理，因此 Cglib2AopProxy 可以为任何类创建代理。
 *
 * @author zhangpengjun
 * @date 2023/4/7
 */
public class Cglib2AopProxy implements AopProxy {

    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {
        private final AdvisedSupport advised;

        private DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            Object targetObject = advised.getTargetSource().getTarget();
            MethodMatcher methodMatcher = advised.getMethodMatcher();
            org.aopalliance.intercept.MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(targetObject, method, args, proxy);
            if (methodMatcher.matches(method, targetObject.getClass())) {
                return methodInterceptor.invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return methodProxy.invoke(getThis(), getArguments());
        }
    }

}
