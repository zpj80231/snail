package com.sanil.source.code.rpc;

import cn.hutool.core.thread.ThreadUtil;
import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.proxy.RpcClientProxy;
import com.sanil.source.code.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClient {

    /**
     * 用于请求发起，模拟一次请求
     */
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new RpcClientManager());
        HelloService helloService = proxy.getProxyService(HelloService.class);

        // 可随时启动关闭多个服务端，查看客户端负载均衡
        int i = 0;
        while (i <= Integer.MAX_VALUE) {
            ThreadUtil.sleep(1000);
            executorService.execute(new Controller(helloService, "hello-" + i++));
        }
    }

    /**
     * 模拟一个 Controller
     */
    private static class Controller implements Runnable {

        private final HelloService helloService;
        private final String msg;

        public Controller(HelloService helloService, String msg) {
            this.helloService = helloService;
            this.msg = msg;
        }

        @Override
        public void run() {
            String result = helloService.hello(msg);
            log.info("result: {}", result);
        }

    }

}
