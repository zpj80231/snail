package com.snail.springframework.beans.factory.config;

/**
 * 单例 Bean 注册表，提供获取一个 单例 Bean 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例Bean
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    Object getSingleton(String beanName);

}
