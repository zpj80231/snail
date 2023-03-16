package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.factory.config.BeanDefinitionRegistry;
import com.snail.springframework.core.io.DefaultResourceLoader;
import com.snail.springframework.core.io.ResourceLoader;

/**
 * 主要提供对 getRegistry()、getResourceLoader() 的默认实现，子类只需关心加载Bean定义即可。
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private final ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
