package com.snail.springframework.beans.factory;

/**
 * Bean 工厂
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public interface BeanFactory {

    /**
     * 获取一个 Bean，通过无参构造器
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    Object getBean(String beanName);

    /**
     * 获取一个 Bean，通过有参构造器
     *
     * @param beanName bean名字
     * @param args     bean有参构造器参数
     * @return {@link Object}
     */
    Object getBean(String beanName, Object... args);

}
