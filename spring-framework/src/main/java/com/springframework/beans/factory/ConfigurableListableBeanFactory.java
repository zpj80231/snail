package com.springframework.beans.factory;

import com.springframework.beans.factory.config.BeanDefinition;

/**
 * ListableBeanFactory 和 AutowireCapableBeanFactory 接口的扩展
 * 支持 获取 BeanDefinition 和 预实例化所有单例 bean。
 * 可以更方便地配置和管理 bean，并对其进行更高级别的操作，例如自动装配、作用域管理、属性解析等。这使得应用程序具有更高的灵活性和可维护性。
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 得到 BeanDefinition
     *
     * @param beanName bean名字
     * @return {@link BeanDefinition}
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 预实例化所有单例 Bean
     */
    void preInstantiateSingletons();

}
