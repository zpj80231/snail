package com.snail.springframework.beans.factory.config;

import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * 用于修改 Spring 容器中 BeanDefinition 的扩展点。
 * 在 Spring 容器创建 BeanDefinition 之后，但在创建 Bean 实例之前。
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

}
