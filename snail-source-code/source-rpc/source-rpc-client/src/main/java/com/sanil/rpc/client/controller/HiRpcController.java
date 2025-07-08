package com.sanil.rpc.client.controller;

import cn.hutool.core.util.RandomUtil;
import com.sanil.rpc.api.HiService;
import com.sanil.source.code.rpc.client.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpj
 * @date 2025/7/8
 */
@Slf4j
@RestController
public class HiRpcController {

    @RpcReference
    private HiService hiService;
    @RpcReference(group = "dev", version = "1.0.0")
    private HiService hiServiceDev;
    @RpcReference(group = "uat", version = "2.0.0")
    private HiService hiServiceUat;
    @RpcReference(group = "unknow", version = "unknow")
    private HiService hiServiceUnknow;

    /**
     * 刷新页面查看rpc调用结果，http://127.0.0.1:8022/hi
     *
     * @return {@link String }
     */
    @GetMapping("/hi")
    public String hi() {
        String msg = "hello world";
        int randomInt = RandomUtil.randomInt(15);
        String result;
        if (randomInt <= 4) {
            result = hiService.hi(msg);
        } else if (randomInt <= 8) {
            result = hiServiceDev.hi(msg);
        } else if (randomInt <= 13){
            result = hiServiceUat.hi(msg);
        } else {
            // 未知服务
            result = hiServiceUnknow.hi(msg);
        }
        log.info("hi result: {}", result);
        return result;
    }

}
