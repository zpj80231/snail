package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.DisposableBean;
import com.snail.springframework.beans.factory.config.SingletonBeanRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实现了添加和获取单例 Bean 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    @Override
    public void destroySingletons() {
        Set<String> keySet = disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new BeansException("Failed to bean:[" + beanName + "] destroy", e);
            }
        }

    }

}
