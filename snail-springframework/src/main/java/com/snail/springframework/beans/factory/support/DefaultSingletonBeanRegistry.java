package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.ObjectFactory;
import com.snail.springframework.beans.factory.DisposableBean;
import com.snail.springframework.beans.factory.config.SingletonBeanRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 一级缓存，一级缓存存储已经完全创建和初始化的单例 Bean 的实例。
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存，用于存储正在创建中的 Bean 的实例（属性尚未填填充）。
     * 当 Spring 创建 Bean 时，如果发现该 Bean 存在循环依赖，它会将 Bean 的实例放入这个缓存中。这个实例可以被其他 Bean 提前获取，以解决循环依赖问题。
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * 三级缓存，存储的是 Bean 的工厂对象（ObjectFactory）。
     * 当 Spring 创建 Bean 时，如果发现该 Bean 存在循环依赖，它会将 Bean 的工厂对象放入这个缓存中。这个工厂对象用于在后续创建 Bean 的过程中获取 Bean 的实例。
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        // 先从一级缓存获取
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 再从二级缓存获取
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                // 最后从三级缓存获取
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    // 三级缓存存在，放入二级缓存
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> objectFactory) {
        if (!singletonFactories.containsKey(beanName)) {
            singletonFactories.put(beanName, objectFactory);
            earlySingletonObjects.remove(beanName);
        }
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
