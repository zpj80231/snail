package com.snail.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author zhangpengjun
 * @date 2023/9/15
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Qualifier {

    String value() default "";

}
