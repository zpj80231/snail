package com.sanil.source.code.rpc.server.annotation;

import com.sanil.source.code.rpc.spring.RpcServiceScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Target(ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Import(RpcServiceScannerRegistrar.class)
public @interface EnableRpcServer {

    String value() default "";

    String[] basePackage() default {};

}
