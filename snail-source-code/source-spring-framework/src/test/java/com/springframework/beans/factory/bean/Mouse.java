package com.springframework.beans.factory.bean;

import com.springframework.beans.factory.BeanClassLoaderAware;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.beans.factory.BeanNameAware;
import com.springframework.context.ApplicationContext;
import com.springframework.context.ApplicationContextAware;

/**
 * @author zhangpengjun
 * @date 2023/3/28
 */
public class Mouse implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String name;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("==> BeanClassLoaderAware classLoader: " + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        System.out.println("==> BeanFactoryAware beanFactory: " + beanFactory);
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("==> BeanNameAware beanName: " + beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("==> ApplicationContextAware applicationContext: " + applicationContext);
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mouse{" +
                "applicationContext=" + applicationContext +
                ", name='" + name + '\'' +
                '}';
    }
}
