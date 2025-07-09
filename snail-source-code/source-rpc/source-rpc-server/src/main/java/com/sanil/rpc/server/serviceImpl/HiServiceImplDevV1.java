package com.sanil.rpc.server.serviceImpl;

import cn.hutool.core.util.StrUtil;
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
        return StrUtil.join("<br><br>", name, getClass().getName(), "group:dev version:1.0.0");
    }

}
