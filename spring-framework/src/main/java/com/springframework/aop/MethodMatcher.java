package com.springframework.aop;

import java.lang.reflect.Method;

/**
 * 在 AOP 中，MethodMatcher 主要作用是对目标类的方法进行筛选和匹配，以便确定哪些方法需要被拦截和增强。
 * 通常情况下，MethodMatcher 总是和 ClassFilter 一起使用，以实现更加细粒度的 AOP 控制。
 * <p>
 * 例如，在 Spring AOP 中，可以通过 Pointcut 来定义一个切点，然后使用 MethodMatcher 进行方法匹配。
 * 在匹配过程中，Spring 将会遍历所有的方法，将每个方法传递给 MethodMatcher 的 matches 方法进行匹配。
 * 如果这个方法符合条件，则将其加入到代理列表中，否则跳过不处理。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public interface MethodMatcher {

    /**
     * 用于在代理对象上进行目标方法匹配，判断是否符合条件
     *
     * @param method      方法
     * @param targetClass 目标类
     * @return boolean
     */
    boolean matches(Method method, Class<?> targetClass);

}
