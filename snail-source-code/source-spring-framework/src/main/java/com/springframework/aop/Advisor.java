package com.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 一个 aop 操作的访问者，持有要执行的具体操作。
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public interface Advisor {

    /**
     * 获取一个准备好的 aop 执行器
     *
     * @return {@link Advice}
     */
    Advice getAdvice();

}
