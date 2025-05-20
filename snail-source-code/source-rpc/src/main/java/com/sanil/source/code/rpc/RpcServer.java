package com.sanil.source.code.rpc;

import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@EnableRpcServer
public class RpcServer {

    public static void main(String[] args) {
        // 启动一个服务端，默认端口为 8023
        // new RpcServerManager().start();

        // 启动多个服务端，模拟serviceImpl在多个服务器上，客户端自动负载均衡
        new Thread(() -> new RpcServerManager().start()).start();
        new Thread(() -> new RpcServerManager(8024).start()).start();
        new Thread(() -> new RpcServerManager(8025).start()).start();
    }

}
