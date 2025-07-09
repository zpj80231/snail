package com.sanil.rpc.server.serviceImpl;

import cn.hutool.core.util.StrUtil;
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
        return StrUtil.join("<br><br>", name, getClass().getName(), "group:uat version:2.0.0");
    }

}
