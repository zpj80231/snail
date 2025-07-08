package com.sanil.source.code.rpc;

import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.spring.EnableRpcService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 示例：RPC服务端和Spring集成，使用方式
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@EnableRpcService
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
