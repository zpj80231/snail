package com.sanil.source.code.rpc;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.proxy.RpcClientProxy;
import com.sanil.source.code.rpc.client.proxy.RpcReferenceBuilder;
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
        RpcClientManager rpcClientManager = new RpcClientManager();

        // 测试1：默认代理，加 @RpcReference 的话，按注解的分组和版本号走
        RpcClientProxy proxy = new RpcClientProxy(rpcClientManager);
        HelloService helloService = proxy.getProxyService(HelloService.class);

        // 测试2：为代理手动指定分组和版本，模拟加了 @RpcReference 情况 (方便和 Spring 集成)
        RpcReferenceBuilder rpcReferenceBuilder = RpcReferenceBuilder.builder()
                .group("uat").version("2.0.0").service(HelloService.class).build();
        RpcClientProxy proxyRpcReference = new RpcClientProxy(rpcClientManager, rpcReferenceBuilder);
        HelloService helloServiceV2 = proxyRpcReference.getProxyService(HelloService.class);

        // 测试3：可随时启动关闭多个服务端，查看客户端自动负载均衡
        int i = 0;
        while (i <= Integer.MAX_VALUE) {
            ThreadUtil.sleep(1000);
            if (RandomUtil.randomInt(10) > 7) {
                executorService.execute(new Controller(helloServiceV2, " --> 手动hi-模拟@RpcReference-" + i++));
                continue;
            }
            executorService.execute(new Controller(helloService, " --> 默认hi-" + i++));
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
