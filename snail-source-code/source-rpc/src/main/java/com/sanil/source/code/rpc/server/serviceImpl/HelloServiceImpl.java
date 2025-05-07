package com.sanil.source.code.rpc.server.serviceImpl;

import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.service.HelloService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return name + " hello!";
    }

}
