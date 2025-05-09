package com.sanil.source.code.rpc;

import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.proxy.RpcClientServiceProxy;
import com.sanil.source.code.rpc.server.serviceImpl.HelloServiceImpl;
import com.sanil.source.code.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        RpcClientManager rpcClientManager = new RpcClientManager();
        RpcClientChannel channel = rpcClientManager.connect();

        HelloService helloService = new RpcClientServiceProxy(channel).getProxyService(HelloServiceImpl.class);
        String result = helloService.hello("hi");
        log.info("result: {}", result);
    }

}
