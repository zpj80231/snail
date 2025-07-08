package com.sanil.source.code.rpc;

import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;

/**
 * 示例：RPC 服务端启动，使用方式
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@EnableRpcServer
public class RpcServer {

    public static void main(String[] args) {
        // 启动一个服务端，默认端口为 8023
        new RpcServerManager().start();
    }

}
