package com.snail.springframework.beans.factory;

import com.snail.springframework.beans.factory.config.BeanPostProcessor;
import com.snail.springframework.beans.factory.config.SingletonBeanRegistry;
import com.snail.springframework.util.StringValueResolver;

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

    /**
     * 为嵌入式值（例如注释属性）添加字符串解析器
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析给定的嵌入式值，例如一个注解属性
     */
    String resolveEmbeddedValue(String value);

}
