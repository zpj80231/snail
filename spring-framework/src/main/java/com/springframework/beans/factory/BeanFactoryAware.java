package com.springframework.beans.factory;

/**
 * 实现该接口的 bean 可以获取 BeanFactory（即Spring容器）对象。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface BeanFactoryAware extends Aware {

    /**
     * 感知 BeanFactory（即Spring容器）对象
     *
     * @param beanFactory bean工厂
     */
    void setBeanFactory(BeanFactory beanFactory);

}
