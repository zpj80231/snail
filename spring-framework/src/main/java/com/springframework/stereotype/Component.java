package com.springframework.stereotype;

import java.lang.annotation.*;

/**
 * @author zhangpengjun
 * @date 2023/9/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";

}
