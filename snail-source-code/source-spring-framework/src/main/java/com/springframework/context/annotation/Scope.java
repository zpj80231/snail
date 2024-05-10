package com.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 用于配置 bean 的作用域，默认单例
 *
 * @author zhangpengjun
 * @date 2023/9/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "singleton";

}
