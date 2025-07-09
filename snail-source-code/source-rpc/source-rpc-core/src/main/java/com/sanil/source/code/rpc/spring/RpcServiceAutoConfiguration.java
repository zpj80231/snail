package com.sanil.source.code.rpc.spring;

import com.sanil.source.code.rpc.server.RpcServerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RPC 服务自动配置
 *
 * @author zhangpj
 * @date 2025/07/09
 */
@Configuration
public class RpcServiceAutoConfiguration {

    @Bean(initMethod = "startAsync", destroyMethod = "destroy")
    public RpcServerManager rpcServerManager() {
        RpcServerManager manager = new RpcServerManager();
        manager.setAutoRegister(false);
        return manager;
    }

    @Bean
    public RpcServiceBeanPostProcessor rpcServiceBeanPostProcessor(RpcServerManager rpcServerManager) {
        return new RpcServiceBeanPostProcessor(rpcServerManager);
    }

}
