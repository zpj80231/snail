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
        RpcServerManager rpcServerManager = new RpcServerManager();
        rpcServerManager.start();
    }

}
