package com.snail.springframework.beans.factory;

import java.util.Map;

/**
 * 扩展 BeanFactory 接口，支持获取多个 Bean 或者与多个 Bean 信息相关的场景
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 根据类型获取 Spring 容器中所有对应类型的 Bean 实例
     *
     * @param type 类型
     * @return {@link Map}<{@link String}, {@link T}>
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);

    /**
     * 获取 Spring 容器中所有 BeanDefinition 的名称
     *
     * @return {@link String[]}
     */
    String[] getBeanDefinitionNames();

}
