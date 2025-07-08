package com.sanil.rpc.server.serviceImpl;

import com.sanil.rpc.api.HiService;
import com.sanil.source.code.rpc.server.annotation.RpcService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService(group = "dev", version = "1.0.0")
public class HiServiceImplDevV1 implements HiService {

    @Override
    public String hi(String name) {
        return "hi-rpc group:dev version:1.0.0 " + name;
    }

}
