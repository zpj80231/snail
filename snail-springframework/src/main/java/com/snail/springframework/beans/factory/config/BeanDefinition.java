package com.snail.springframework.beans.factory.config;

/**
 * BeanDefinition 定义
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class BeanDefinition {

    /**
     * 定义为 Class，这样就可以把 Bean 的实例化（通过反射）操作放到容器中处理了
     */
    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
