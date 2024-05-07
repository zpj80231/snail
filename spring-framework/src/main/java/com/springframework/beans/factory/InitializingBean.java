package com.springframework.beans.factory;

/**
 * 如果 bean 实现了 InitializingBean 接口，容器会调用它的 afterPropertiesSet() 方法，完成 bean 的初始化。
 * 如果 bean 没有实现 InitializingBean 接口，容器会调用配置文件中指定的初始化方法（如果有）
 *
 * @author zhangpengjun
 * @date 2023/3/24
 */
public interface InitializingBean {

    /**
     * Bean 属性填充后执行
     */
    void afterPropertiedSet();

}
