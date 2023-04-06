package com.snail.springframework.beans.factory.config;

import com.snail.springframework.beans.factory.DisposableBean;

/**
 * 单例 Bean 注册表，提供获取一个 单例 Bean 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public interface SingletonBeanRegistry {

    /**
     * 注册单例 Bean
     *
     * @param beanName        bean名字
     * @param singletonObject 单例对象
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 获取单例 Bean
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    Object getSingleton(String beanName);

    /**
     * 注册待销毁的单例对象
     *
     * @param beanName bean名字
     * @param bean     待销毁的 bean
     */
    void registerDisposableBean(String beanName, DisposableBean bean);

    /**
     * 销毁单例对象
     */
    void destroySingletons();

}
