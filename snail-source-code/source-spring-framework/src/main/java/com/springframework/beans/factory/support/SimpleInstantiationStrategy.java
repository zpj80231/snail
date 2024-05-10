package com.springframework.beans.factory.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * JDK 实例化
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiatie(BeanDefinition beanDefinition, String beanName, Constructor constructor,
                               Object[] args) throws BeansException {
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            if (constructor != null) {
                return clazz.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }

}
