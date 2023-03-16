package com.snail.springframework.beans.factory.config;

import com.snail.springframework.beans.PropertyValues;

/**
 * BeanDefinition 定义
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class BeanDefinition {

    /**
     * 定义为 Class，这样就可以把 Bean 的实例化（通过反射）操作放到容器中处理了，实例化用
     */
    private Class<?> beanClass;

    /**
     * 一个 Bean 的所有字段信息，留作属性填充用
     */
    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
