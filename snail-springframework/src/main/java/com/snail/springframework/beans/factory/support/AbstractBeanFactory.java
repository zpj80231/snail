package com.snail.springframework.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import com.snail.springframework.beans.factory.ConfigurableBeanFactory;
import com.snail.springframework.beans.factory.FactoryBean;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 具备注册、获取 Bean 的能力
 * implements ConfigurableBeanFactory（这里实现了 添加 BeanPostProcessor 后置处理器）
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /**
     * Bean 的类加载器
     */
    private final ClassLoader beanClassLoader = ClassUtil.getClassLoader();

    /**
     * 保存所有注册的 BeanPostProcessor，在 Bean 实例化及属性填充后，初始化时，干预 Bean 的创建过程
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * 模板方法
     * 1. 对单例 Bean 的获取
     * 2. 没有获取到的单例 Bean（可能是多例也可能是多例的），则根据 Bean 定义留给子类去创建
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    protected <T> T doGetBean(final String name, final Object[] args) {
        String beanName = transformedBeanName(name);
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            // 如果是 FactoryBean，那么就转换为 目标bean 返回
            return (T) getObjectForBeanInstance(sharedInstance, name, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object bean = createBean(beanName, beanDefinition, args);
        // 如果是 FactoryBean，那么就转换为 目标bean 返回
        return (T) getObjectForBeanInstance(bean, name, beanName);
    }

    /**
     * 获取：去除 & 后的 beanName
     *
     * @param name 名字
     * @return {@link String}
     */
    private String transformedBeanName(String name) {
        if (name.startsWith("&")) {
            return name.substring(name.indexOf("&") + 1);
        }
        return name;
    }

    /**
     * 获得bean实例对象
     * 获取指定实例对应的目标 bean，如果该实例是一个 FactoryBean，则调用其 getObject() 方法来获取目标 bean。
     *
     * @param beanInstance bean实例
     * @param name         bean原始名字
     * @param beanName     bean名字
     * @return {@link Object}
     */
    private Object getObjectForBeanInstance(Object beanInstance, String name, String beanName) {
        if (!(beanInstance instanceof FactoryBean) || name.startsWith("&")) {
            return beanInstance;
        }
        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return object;
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName, args);
    }

    /**
     * 得到 Bean 定义
     *
     * @param beanName bean名字
     * @return {@link BeanDefinition}
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);

    /**
     * 创建 Bean
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     * @param args
     * @return {@link Object}
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }
}
