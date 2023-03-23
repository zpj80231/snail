package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.config.BeanDefinitionRegistry;
import com.snail.springframework.core.io.Resource;
import com.snail.springframework.core.io.ResourceLoader;

/**
 * 从各个资源加载为 BeanDefinition
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public interface BeanDefinitionReader {

    /**
     * 获得所有 BeanDefinition 注册
     *
     * @return {@link BeanDefinitionRegistry}
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 获得资源加载器
     *
     * @return {@link ResourceLoader}
     */
    ResourceLoader getResourceLoader();

    /**
     * 加载bean定义
     *
     * @param resource 资源
     * @throws BeansException 豆子例外
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 加载bean定义
     *
     * @param resources 资源
     * @throws BeansException 豆子例外
     */
    void loadBeanDefinitions(Resource... resources) throws BeansException;

    /**
     * 加载bean定义
     *
     * @param location 位置
     * @throws BeansException 豆子例外
     */
    void loadBeanDefinitions(String location) throws BeansException;

    /**
     * 加载bean定义
     *
     * @param locations 位置
     * @throws BeansException 豆子例外
     */
    void loadBeanDefinitions(String... locations) throws BeansException;

}
