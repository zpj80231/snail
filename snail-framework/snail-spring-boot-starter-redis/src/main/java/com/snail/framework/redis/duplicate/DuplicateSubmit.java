package com.snail.framework.redis.duplicate;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DuplicateSubmit {

    /**
     * SpEl key，默认：类名#方法名
     *
     * @return {@link String }
     */
    String key() default "";

    /**
     * 过期时间
     *
     * @return long
     */
    long expire() default 1L;

    /**
     * 时间单位
     *
     * @return {@link TimeUnit }
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 提示消息
     *
     * @return {@link String }
     */
    String message() default "操作过于频繁，请稍后再试";

}
