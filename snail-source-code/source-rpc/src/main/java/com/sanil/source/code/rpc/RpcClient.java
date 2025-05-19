package com.sanil.source.code.rpc;

import cn.hutool.core.thread.ThreadUtil;
import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.proxy.RpcClientProxy;
import com.sanil.source.code.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        RpcClientManager rpcClientManager = new RpcClientManager();
        RpcClientProxy proxy = new RpcClientProxy(rpcClientManager);

        // 模拟100次请求
        for (int i = 0; i < 100; i++) {
            // 根据接口创建代理，进行远程调用
            HelloService helloService = proxy.getProxyService(HelloService.class);
            String result = helloService.hello("hi-" + i);
            log.info("result: {}", result);
        }

        ThreadUtil.sleep(35, TimeUnit.SECONDS);
        rpcClientManager.shutdown();
    }

}
