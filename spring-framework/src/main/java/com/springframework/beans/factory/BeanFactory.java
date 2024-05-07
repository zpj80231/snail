package com.springframework.beans.factory;

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

    /**
     * 获取一个指定类型的 Bean
     *
     * @param beanName     bean名字
     * @param requiredType bean类型
     * @return {@link T}
     */
    <T> T getBean(String beanName, Class<T> requiredType);

    /**
     * 获取一个指定类型的 Bean，按照指定类型类型
     *
     * @param requiredType 字段类型
     * @return {@link Object}
     */
    <T> T getBean(Class<T> requiredType);

    /**
     * 是否包含 bean
     *
     * @param name 名称
     * @return boolean
     */
    boolean containsBean(String name);

}
