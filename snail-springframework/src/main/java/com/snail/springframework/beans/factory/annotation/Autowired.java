package com.snail.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author zhangpengjun
 * @date 2023/9/15
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

}
