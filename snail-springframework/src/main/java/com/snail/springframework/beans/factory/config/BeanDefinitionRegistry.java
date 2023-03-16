package com.snail.springframework.beans.factory.config;

/**
 * Bean 定义的注册表，提供获取一个注册 BeanDefinition 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册 BeanDefinition
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 是否包含 BeanDefinition
     *
     * @param beanName bean名字
     * @return boolean
     */
    boolean containsBeanDefinition(String beanName);

}
