package com.sanil.source.code.rpc.spring;

import com.sanil.source.code.rpc.server.RpcServerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * RPC 服务器自动启动
 *
 * @author zhangpj
 * @date 2025/7/9
 */
@Slf4j
public class RpcServerAutoStarter implements InitializingBean, DisposableBean {

    private RpcServerManager rpcServerManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.rpcServerManager = new RpcServerManager();
        this.rpcServerManager.startAsync();
    }

    @Override
    public void destroy() throws Exception {
        if (rpcServerManager != null) {
            rpcServerManager.destroy();
        }
    }

}
