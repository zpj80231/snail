package com.sanil.source.code.rpc.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 RPC 扫描
 * <p>
 * 自动扫描 @RpcService 作为 Spring 组件
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServiceBeanPostProcessor.class)
public @interface EnableRpcScan {

    String[] value() default {};

    String[] basePackage() default {};

}
