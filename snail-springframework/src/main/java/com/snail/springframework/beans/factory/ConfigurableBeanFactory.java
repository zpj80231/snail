package com.snail.springframework.beans.factory;

import com.snail.springframework.beans.factory.config.BeanPostProcessor;
import com.snail.springframework.beans.factory.config.SingletonBeanRegistry;

/**
 * 扩展 BeanFactory 接口，支持设置父级容器、自自定义作用域、添加 Bean 后置处理器、设置 bean 的初始化和销毁回调方法等
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
