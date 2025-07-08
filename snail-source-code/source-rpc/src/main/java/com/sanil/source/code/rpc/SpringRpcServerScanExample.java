package com.sanil.source.code.rpc;

import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.spring.EnableRpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 示例：RPC服务端 和 Spring集成
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@EnableRpcScan
public class SpringRpcServerScanExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringRpcServerScanExample.class);
        SpringRpcServerManager manager = context.getBean(SpringRpcServerManager.class);
        manager.start();
    }

    @Component
    static class SpringRpcServerManager extends RpcServerManager {

    }

}
