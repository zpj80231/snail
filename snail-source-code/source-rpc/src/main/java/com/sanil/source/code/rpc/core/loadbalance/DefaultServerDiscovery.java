package com.sanil.source.code.rpc.core.loadbalance;

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

    public DefaultServerDiscovery(ServerRegistry serverRegistry) {
        this(new RoundRobinLoadBalance(), serverRegistry);
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
