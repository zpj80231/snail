package com.sanil.rpc.server;

import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.spring.EnableRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangpj
 * @date 2025/7/8
 */
@EnableRpcService
@SpringBootApplication
public class SourceRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceRpcServerApplication.class, args);
        new RpcServerManager().startAsync();
    }

}
