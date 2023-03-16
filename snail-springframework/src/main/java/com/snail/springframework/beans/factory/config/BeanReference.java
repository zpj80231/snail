package com.snail.springframework.beans.factory.config;

/**
 * Bean 引用
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class BeanReference {

    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

}
