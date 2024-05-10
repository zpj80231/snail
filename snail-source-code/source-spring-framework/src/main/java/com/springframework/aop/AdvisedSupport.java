package com.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * AdvisedSupport，主要是用于把代理、拦截、匹配的各项属性包装到一个类中，方便在 AopProxy 实现类进行使用。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public class AdvisedSupport {

    /**
     * 被代理对象
     */
    private TargetSource targetSource;

    /**
     * 方法拦截器，拦截方法实现类，由用户自己实现
     */
    private MethodInterceptor methodInterceptor;

    /**
     * 方法匹配器
     */
    private MethodMatcher methodMatcher;

    /**
     * 是否通过 类 代理对象（cglib代理？true：false）
     */
    private boolean proxyTargetClass = false;

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }


    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
