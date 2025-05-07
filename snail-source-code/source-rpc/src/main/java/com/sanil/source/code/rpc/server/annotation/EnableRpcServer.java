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
public @interface EnableRpcServer {

    String scan() default "";

}
