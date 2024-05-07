package com.springframework.beans.factory.config;

import com.springframework.beans.PropertyValues;
import com.springframework.beans.factory.ConfigurableBeanFactory;

/**
 * BeanDefinition 定义
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class BeanDefinition {

    private static final String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    private static final String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 定义为 Class，这样就可以把 Bean 的实例化（通过反射）操作放到容器中处理了，实例化用
     */
    private Class<?> beanClass;
    /**
     * 一个 Bean 的所有字段信息，留作属性填充用
     */
    private PropertyValues propertyValues;
    /**
     * init方法名称
     */
    private String initMethodName;
    /**
     * 销毁方法名称
     */
    private String destroyMethodName;

    /**
     * 生命周期，默认单例的
     */
    private String scope = SCOPE_SINGLETON;
    /**
     * 单例，默认是
     */
    private boolean singleton = true;
    /**
     * 原型，默认不是
     */
    private boolean prototype = false;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public String getScope() {
        return scope;
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

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }
}
