package com.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author zhangpengjun
 * @date 2023/9/15
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * The actual value expression: e.g. "${systemProperties.myProp}".
     */
    String value();

}
