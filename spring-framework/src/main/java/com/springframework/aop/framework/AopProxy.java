package com.springframework.aop.framework;

/**
 * AopProxy 接口是实现 AOP 的关键，它用于创建一个代理对象来拦截目标对象的方法调用，并将增强逻辑嵌入到方法调用过程中。
 * 通过 AopProxy，可以将任何普通的 Java 对象转换成带有切面的代理对象，从而实现 AOP 功能。
 * <p>
 * Spring 提供了多种类型的 AopProxy 实现，包括 JDK 动态代理和 CGLIB 代理。在使用 Spring 进行 AOP 编程时，可以根据需要选择不同的代理实现来生成代理对象。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public interface AopProxy {

    /**
     * 获得代理
     *
     * @return {@link Object}
     */
    Object getProxy();

}
