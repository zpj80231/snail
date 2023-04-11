package com.snail.springframework.aop;

/**
 * 在 AOP 中，ClassFilter 用于在 BeanFactory 和 ApplicationContext 中过滤出需要被代理的类，
 * 主要作用是对目标类进行筛选和匹配，以便确定哪些类需要被代理和增强。
 * <p>
 * 例如，在 Spring AOP 中，可以使用切点（Pointcut）来定义一个需要拦截的切点，然后使用 ClassFilter 对目标类进行筛选和匹配。
 * 在匹配过程中，Spring 将会遍历所有的类，将每个类传递给 ClassFilter 的 matches 方法进行匹配。
 * 如果这个类符合条件，则将其加入到代理列表中，否则跳过不处理。
 *
 * @author zhangpengjun
 * @date 2023/4/6
 */
public interface ClassFilter {

    /**
     * 用于判断指定的 Class 对象是否符合条件
     *
     * @param clazz clazz
     * @return boolean
     */
    boolean matches(Class<?> clazz);

}
