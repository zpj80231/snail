package com.snail.springframework.beans.factory;

/**
 * Spring Aware 接口是一个标记接口，实现该接口的 Bean 可以访问 Spring 的核心组件 ApplicationContext 或其他特定的 Spring 框架对象。
 * 通过实现 Spring Aware 接口，bean 可以获取 Spring 容器中的其他 bean 和资源，从而更好地利用 Spring 框架的功能。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface Aware {
    
}
