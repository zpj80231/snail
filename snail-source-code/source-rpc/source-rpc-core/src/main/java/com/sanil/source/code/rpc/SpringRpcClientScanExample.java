package com.sanil.source.code.rpc;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.sanil.source.code.rpc.client.annotation.RpcReference;
import com.sanil.source.code.rpc.server.serviceImpl.HelloServiceImpl;
import com.sanil.source.code.rpc.service.HelloService;
import com.sanil.source.code.rpc.spring.EnableRpcScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 示例：RPC客户端和Spring集成，使用方式
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Slf4j
@EnableRpcScan
public class SpringRpcClientScanExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringRpcClientScanExample.class);
        HelloController helloController = context.getBean(HelloController.class);
        for (;;) {
            try {
                ThreadUtil.sleep(1000);
                helloController.hello("cat");
            } catch (Exception e) {
                log.error("Spring集成RPC调用异常", e);
            }
        }
    }

    @Component
    static class HelloController {
        @RpcReference
        private HelloService helloService;
        @RpcReference(group = "dev", version = "1.0.0")
        private HelloService helloServiceDev;
        @RpcReference(group = "uat", version = "2.0.0")
        private HelloService helloServiceUat;
        @RpcReference(group = "unknow", version = "unknow")
        private HelloService helloServiceUnknow;
        @Resource(type = HelloServiceImpl.class)
        private HelloService helloServiceAutowired;

        public void hello(String msg) {
            int randomInt = RandomUtil.randomInt(15);
            String result;
            if (randomInt <= 4) {
                result = helloService.hello(msg);
            } else if (randomInt <= 8) {
                result = helloServiceDev.hello(msg);
            } else if (randomInt <= 13){
                result = helloServiceUat.hello(msg);
            } else {
                result = helloServiceUnknow.hello(msg); // 未知服务
            }
            log.info("hello result: {}", result);
        }
    }

}
