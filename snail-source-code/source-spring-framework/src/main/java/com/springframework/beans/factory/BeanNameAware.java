package com.springframework.beans.factory;

/**
 * 实现该接口的 bean 可以获取 所属 Bean 在容器中的名称。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface BeanNameAware extends Aware {

    /**
     * 感知 bean 名称
     *
     * @param beanName bean名字
     */
    void setBeanName(String beanName);

}
