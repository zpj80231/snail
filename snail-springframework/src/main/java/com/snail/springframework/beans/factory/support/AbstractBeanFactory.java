package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.factory.BeanFactory;
import com.snail.springframework.beans.factory.config.BeanDefinition;

/**
 * 具备注册、获取 Bean 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 模板方法
     * 1. 对单例 Bean 的获取
     * 2. 没有获取到的单例 Bean（可能是多例也可能是多例的），则根据 Bean 定义留给子类去创建
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    protected <T> T doGetBean(final String beanName, final Object[] args) {
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return (T) bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return (T) createBean(beanName, beanDefinition, args);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName, args);
    }

    /**
     * 得到 Bean 定义
     *
     * @param beanName bean名字
     * @return {@link BeanDefinition}
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);

    /**
     * 创建 Bean
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     * @param args
     * @return {@link Object}
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);

}