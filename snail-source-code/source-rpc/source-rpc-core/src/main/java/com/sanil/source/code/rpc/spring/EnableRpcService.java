package com.sanil.source.code.rpc.spring;

import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 启用 RPC 服务
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Target(ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Import({RpcServiceAutoConfiguration.class, RpcServiceScannerRegistrar.class})
@EnableRpcServer
public @interface EnableRpcService {

    /**
     * value 和 basePackage 互为别名
     * 支持两种配置方式：
     * 1. @EnableRpcClient("com.example.pkg")
     * 2. @EnableRpcClient(basePackage = "com.example.pkg")
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 指定扫描的基础包路径
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * 通过类来指定包路径
     */
    Class<?>[] basePackageClasses() default {};

}
