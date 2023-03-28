package com.snail.springframework.context.support;

import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.snail.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.snail.springframework.beans.factory.config.BeanPostProcessor;
import com.snail.springframework.context.ConfigurableApplicationContext;
import com.snail.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    @Override
    public void refresh() {
        // 1. 创建 Bean 工厂，加载 BeanDefinition
        refreshBeanFactory();

        // 2. 获得 Bean 工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. BeanDefinition 扩展点：
        // Spring 容器中 BeanDefinition 的扩展点。
        // 创建 BeanDefinition 之后，但在创建 Bean 实例之前。
        invokeBeanFactoryPostProcessors(beanFactory);

        // 4. Bean 扩展点：
        // 提前注册所有的 BeanPostProcessor，在 Bean 创建后，初始化时扩展
        // （见 AbstractAutowireCapableBeanFactory.createBean ）。
        registerBeanPostProcessors(beanFactory);

        // 5. 提前实例化所有的单例 Bean
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
