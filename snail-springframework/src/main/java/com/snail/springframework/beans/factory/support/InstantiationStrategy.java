package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public interface InstantiationStrategy {

    /**
     * 实例化
     *
     * @param beanDefinition bean定义
     * @param beanName       bean名字
     * @param constructor    构造函数
     * @param args           构造函数入参参数
     * @return {@link Object}
     */
    Object instantiatie(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args);

}
