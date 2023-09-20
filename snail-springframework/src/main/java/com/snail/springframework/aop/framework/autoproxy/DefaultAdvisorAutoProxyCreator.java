package com.snail.springframework.aop.framework.autoproxy;

import com.snail.springframework.aop.AdvisedSupport;
import com.snail.springframework.aop.Advisor;
import com.snail.springframework.aop.ClassFilter;
import com.snail.springframework.aop.TargetSource;
import com.snail.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.snail.springframework.aop.framework.ProxyFactory;
import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValues;
import com.snail.springframework.beans.factory.BeanFactory;
import com.snail.springframework.beans.factory.BeanFactoryAware;
import com.snail.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.snail.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DefaultAdvisorAutoProxyCreator 是 Spring Framework 中的一个类，负责根据配置的 Advisors 自动为 Bean 创建代理。
 * 它会扫描 bean，查找任何配置的 Advisors，并在必要时为这些 bean 创建动态代理。
 * 在 Spring Framework 中，Advisors 是定义 advice 的对象，例如拦截方法调用额外的行为。
 * <br/>
 * 它的工作原理如下：
 * <br/>
 * 1. DefaultAdvisorAutoProxyCreator 在初始化阶段检查应用上下文中的所有 bean。
 * 2. 它查找配置了 Advisors 的 bean。一个 Advisor 通常由切面逻辑（advice）和切入点（pointcut）组成，切入点用于指定 advice 应该应用的位置。
 * 3. 对于每个配置了 Advisors 的 bean，DefaultAdvisorAutoProxyCreator 创建一个代理对象，该代理对象包装了原始的 bean。
 * 4. 代理对象拦截对 bean 的方法调用，并应用配置的 Advisors 定义的 advice。
 * 5. 当应用上下文中的其他 bean 请求原始 bean 时，它们将接收代理对象而不是原始对象。
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private final Set<String> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return true;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 不匹配当前类，过滤
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (classFilter != null && !classFilter.matches(bean.getClass())) {
                continue;
            }
            // 转换为代理对象返回
            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setTargetSource(new TargetSource(bean));
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setProxyTargetClass(false); // 默认使用 jdk 代理
            return new ProxyFactory(advisedSupport).getProxy();
        }

        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}
