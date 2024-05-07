package com.springframework.aop;

/**
 * 切入点访问者
 * <p>
 * PointcutAdvisor 承担了 Pointcut 和 Advice 的组合，Pointcut 用于获取 JoinPoint，而 Advice 决定于 JoinPoint 执行什么操作。
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取一个切入点
     *
     * @return {@link Pointcut}
     */
    Pointcut getPointcut();

}
