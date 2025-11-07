package com.sanil.source.rpc.server.serviceImpl;

import com.sanil.source.rpc.server.annotation.RpcService;
import com.sanil.source.rpc.service.HelloService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "hello-rpc " + name;
    }

}
