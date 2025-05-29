package com.sanil.source.code.rpc.server.serviceImpl;

import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.service.HelloService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService(group = "dev", version = "1.0.0")
public class HelloServiceImplDevV1 implements HelloService {

    @Override
    public String hello(String name) {
        return "hello-rpc group:dev version:1.0.0 " + name;
    }

}
