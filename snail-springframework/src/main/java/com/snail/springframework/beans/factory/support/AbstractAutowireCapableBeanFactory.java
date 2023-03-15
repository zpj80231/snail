package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.config.BeanDefinition;

/**
 * @author zhangpengjun
 * @date 2023/3/15
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /**
     * 创建 Bean
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     * @param args
     * @return {@link Object}
     * @throws BeansException
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeansException("Failed to create bean: " + beanName, e);
        }
        addSingleton(beanName, bean);
        return bean;
    }

}
