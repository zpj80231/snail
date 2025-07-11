package com.sanil.source.code.rpc.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangpj
 * @date 2025/5/29
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    String serviceName() default "";

    String group() default "";

    String version() default "";

}
