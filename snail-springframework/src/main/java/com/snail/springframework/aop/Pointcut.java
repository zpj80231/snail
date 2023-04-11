package com.snail.springframework.aop;

/**
 * Pointcut 接口是 Spring 框架 AOP（面向切面编程）的一个核心接口，它用于描述一组需要被拦截的方法或类。
 * 在 Spring 中，Pointcut 可以被看作是一个逻辑切面，它包含了需要被增强的方法和类的信息，并可以根据这些信息来决定哪些方法需要被拦截和增强。
 * <p>
 * Pointcut 接口定义了两个方法：getClassFilter() 和 getMethodMatcher()，用于获取类过滤器和方法匹配器
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public interface Pointcut {

    /**
     * 获取类过滤器
     * 类过滤器用于对目标类进行筛选和匹配，以便确定哪些类需要被代理和增强；
     *
     * @return {@link ClassFilter}
     */
    ClassFilter getClassFilter();

    /**
     * 获取方法匹配器
     * 方法匹配器用于对目标类的方法进行筛选和匹配，以便确定哪些方法需要被拦截和增强；
     *
     * @return {@link MethodMatcher}
     */
    MethodMatcher getMethodMatcher();

}
