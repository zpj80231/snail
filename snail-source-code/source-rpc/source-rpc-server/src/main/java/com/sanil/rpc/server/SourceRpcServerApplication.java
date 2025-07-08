package com.sanil.rpc.server;

import cn.hutool.core.thread.ThreadUtil;
import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.spring.EnableRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author zhangpj
 * @date 2025/7/8
 */
@EnableRpcService
@SpringBootApplication
public class SourceRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceRpcServerApplication.class, args);
    }

    @PostConstruct
    public void start() {
        // rpc服务端spring集成：只需异步启动rpc服务
        ThreadUtil.execAsync(() -> new RpcServerManager().start());
    }

}
