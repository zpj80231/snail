package com.sanil.source.code.rpc.server.serviceImpl;

import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.service.HelloService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService(group = "uat", version = "2.0.0")
public class HelloServiceImplUatV2 implements HelloService {

    @Override
    public String hello(String name) {
        return "hello-rpc group:uat version:2.0.0 " + name;
    }

}
