package com.springframework.beans.factory.config;

/**
 * 用于在 bean 初始化前后进行自定义处理。例如添加日志记录、性能监控、事务管理等。
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface BeanPostProcessor {

    /**
     * 初始化前置操作：
     * 我们可以修改 bean 的属性值、添加新的实例等
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     */
    Object postProcessBeforeInitialization(Object bean, String beanName);

    /**
     * 初始化前置操作：
     * 例如验证 bean 的状态、添加代理对象等
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     */
    Object postProcessAfterInitialization(Object bean, String beanName);

}
