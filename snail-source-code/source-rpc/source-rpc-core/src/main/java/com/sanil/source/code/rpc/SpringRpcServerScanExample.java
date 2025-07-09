package com.sanil.source.code.rpc;

import cn.hutool.json.JSONUtil;
import com.sanil.source.code.rpc.spring.EnableRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 示例：RPC服务端和Spring集成，使用方式
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Slf4j
@EnableRpcService
public class SpringRpcServerScanExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringRpcServerScanExample.class);
        log.info("Spring启动成功, context:{}", JSONUtil.toJsonPrettyStr(context.getBeanDefinitionNames()));
    }

}
