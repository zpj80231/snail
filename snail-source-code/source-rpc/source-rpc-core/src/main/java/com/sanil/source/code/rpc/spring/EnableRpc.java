package com.sanil.source.code.rpc.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 RPC 客户端
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EnableRpcClient
@EnableRpcService
public @interface EnableRpc {

}
