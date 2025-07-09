package com.sanil.rpc.server.serviceImpl;

import com.sanil.rpc.api.HiService;
import com.sanil.source.code.rpc.server.annotation.RpcService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService
public class HiServiceImpl implements HiService {

    @Value("${server.port}")
    private String port;

    @Override
    public String hi(String name) {
        return "hi-rpc " + name + ", from port: " + port;
    }

}
