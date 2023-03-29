package com.snail.springframework.beans.factory;

/**
 * FactoryBean 将对应一个工厂类，该工厂类负责创建目标 bean，并返回它的实例。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface FactoryBean<T> {

    /**
     * 获取对象
     */
    T getObject();

    /**
     * 获取对象类型
     */
    Class<?> getObjectType();

    /**
     * 是否为单例
     */
    boolean isSingleton();

}
