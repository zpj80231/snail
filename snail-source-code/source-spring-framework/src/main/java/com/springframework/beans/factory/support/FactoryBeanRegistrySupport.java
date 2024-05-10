package com.springframework.beans.factory.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存所有 FactoryBean 目标 bean 的能力（单例才缓存），实现 FactoryBean 目标 bean 获取和缓存。
 * 这个类是 Spring 容器的一个关键组件，主要负责处理所有的 FactoryBean 对象，并将它们转化为普通的 bean 实例。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    /**
     * 对所有的 单例FactoryBean 包装的 真实类，进行缓存
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 直接从缓存中获取真实的被FactoryBean包装的单例对象
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    /**
     * 从FactoryBean获取真实的被FactoryBean包装的单例对象，如果是单例Bean还会将查出的结果放入缓存中
     *
     * @param factoryBean 工厂bean
     * @param beanName    bean名字
     * @return {@link Object}
     */
    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            Object object = factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                factoryBeanObjectCache.put(beanName, (object == null ? NULL_OBJECT : object));
            }
            return (object != NULL_OBJECT ? object : null);
        } else {
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }
    }

    private Object doGetObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("Faild to bean[" + beanName + "] doGetObjectFromFactoryBean", e);
        }
    }

}
