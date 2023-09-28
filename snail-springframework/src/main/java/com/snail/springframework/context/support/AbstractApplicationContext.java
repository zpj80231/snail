package com.snail.springframework.context.support;

import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.snail.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.snail.springframework.beans.factory.config.BeanPostProcessor;
import com.snail.springframework.context.ApplicationEventMulticaster;
import com.snail.springframework.context.ApplicationListener;
import com.snail.springframework.context.ConfigurableApplicationContext;
import com.snail.springframework.context.event.ApplicationEvent;
import com.snail.springframework.context.event.ContextClosedEvent;
import com.snail.springframework.context.event.ContextRefreshedEvent;
import com.snail.springframework.context.event.SimpleApplicationEventMulticaster;
import com.snail.springframework.core.convert.ConversionService;
import com.snail.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() {
        // 1. 创建 Bean 工厂，加载 BeanDefinition
        refreshBeanFactory();

        // 2. 获得 Bean 工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 6. 添加 ApplicationContextAware 的后置处理器
        // 使 Bean 创建时（BeanPostProcessor前置处理器）的时候可以感知到容器上下文
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 3. BeanDefinition 扩展点：
        // Spring 容器中 BeanDefinition 的扩展点。
        // 创建 BeanDefinition 之后，但在创建 Bean 实例之前。
        invokeBeanFactoryPostProcessors(beanFactory);

        // 4. Bean 扩展点：
        // 提前注册所有的 BeanPostProcessor，在 Bean 创建后，初始化时扩展
        // （见 AbstractAutowireCapableBeanFactory.createBean ）。
        registerBeanPostProcessors(beanFactory);

        // 7. 初始化事件多播器
        initApplicationEventMulticaster();

        // 8. 注册所有的事件监听器
        registerListeners();

        // 5. 提前实例化所有的单例 Bean + 10. 设置类型转换器
        finishBeanFactoryInitialization(beanFactory);

        // 9. 所有工作已就绪，发布容器刷新完成事件
        finishRefresh();
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        // 10. 设置类型转换器服务器
        if (beanFactory.containsBean("conversionService")) {
            Object conversionService = getBean("conversionService");
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }

        // 5. 提前实例化所有的单例 Bean
        beanFactory.preInstantiateSingletons();
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
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
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));
        // 执行各个单例 Bean 的销毁方法
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

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }
}
