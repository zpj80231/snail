package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.DisposableBean;
import com.snail.springframework.beans.factory.config.SingletonBeanRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 保存了所有单例 bean 和待销毁的 bean
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 空单例对象的内部标记，用作 ConcurrentHashMap（不支持null值）的标记值。
     */
    protected static final Object NULL_OBJECT = new Object();

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
