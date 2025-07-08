package com.sanil.rpc.server.serviceImpl;

import com.sanil.rpc.api.HiService;
import com.sanil.source.code.rpc.server.annotation.RpcService;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@RpcService(group = "uat", version = "2.0.0")
public class HiServiceImplUatV2 implements HiService {

    @Override
    public String hi(String name) {
        return "hi-rpc group:uat version:2.0.0 " + name;
    }

}
