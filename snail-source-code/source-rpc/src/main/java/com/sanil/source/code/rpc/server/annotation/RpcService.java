package com.sanil.source.code.rpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Target(ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 默认为提供者的接口名，可以自定义
     *
     * @return {@link String }
     */
    String name() default "";

    /**
     * 同一接口有多实现的话，可以分组。如：dev、uat、prod
     *
     * @return {@link String }
     */
    String group() default "";

    /**
     * 同一接口有多实现的话，可以分版本。如：1.0.0、1.0.1
     *
     * @return {@link String }
     */
    String version() default "";

}
