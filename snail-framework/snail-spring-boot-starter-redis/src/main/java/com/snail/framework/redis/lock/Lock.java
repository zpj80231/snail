package com.snail.framework.redis.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * key
     *
     * @return {@link String }
     */
    @AliasFor("key")
    String value() default "";

    /**
     * key，默认：类名#方法名
     *
     * @return {@link String }
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 尝试获取锁，最长等待时间，默认一直阻塞等待
     *
     * @return long
     */
    long waitTime() default -1L;

    /**
     * 自动释放时间，默认一直阻塞等待
     *
     * @return long
     */
    long leaseTime() default -1L;

    /**
     * 单位，默认：秒
     *
     * @return {@link TimeUnit }
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}