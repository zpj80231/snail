package com.snail.framework.redis.delay.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法上标识此注解，用于监听延迟队列中的消息
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayQueueListener {

    @AliasFor("queues")
    String[] value() default {};

    @AliasFor("value")
    String[] queues() default {};

}
