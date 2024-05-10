package com.springframework.beans.factory;

/**
 * 扩展 BeanFactory 接口，支持自动装配 Bean 的能力
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 初始化前 bean 的前置处理
     *
     * @param existingBean 现有的bean
     * @param beanName     bean名字
     * @return {@link Object}
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    /**
     * 初始化后 bean 的后置处理
     *
     * @param existingBean 现有的bean
     * @param beanName     bean名字
     * @return {@link Object}
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);

}
