package com.sanil.source.code.rpc.core.loadbalance;

import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.extension.ExtensionLoader;
import com.sanil.source.code.rpc.core.registry.ServerRegistry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
public class DefaultServerDiscovery implements ServerDiscovery {

    private final LoadBalance loadBalance;
    private final ServerRegistry serverRegistry;

    public DefaultServerDiscovery() {
        this(ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(RpcConfig.getLoadBalance()),
                ExtensionLoader.getExtensionLoader(ServerRegistry.class).getExtension(RpcConfig.getServerRegistry()));
    }

    public DefaultServerDiscovery(LoadBalance loadBalance, ServerRegistry serverRegistry) {
        this.loadBalance = loadBalance;
        this.serverRegistry = serverRegistry;
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        Set<InetSocketAddress> servers = serverRegistry.getServerAddress(serviceName);
        return loadBalance.select(servers == null ? new ArrayList<>() : new ArrayList<>(servers));
    }

}
