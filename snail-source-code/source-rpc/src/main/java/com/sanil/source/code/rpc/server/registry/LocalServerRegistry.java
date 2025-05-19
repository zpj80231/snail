package com.sanil.source.code.rpc.server.registry;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class LocalServerRegistry implements ServerRegistry {

    private static final Map<String, InetSocketAddress> SERVER_REGISTRY = new HashMap<>();

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        SERVER_REGISTRY.put(serviceName, serverAddress);
    }

    @Override
    public InetSocketAddress unregister(String serviceName) {
        return SERVER_REGISTRY.remove(serviceName);
    }

    @Override
    public InetSocketAddress getServerAddress(String serviceName) {
        return SERVER_REGISTRY.get(serviceName);
    }

    @Override
    public Map<String, InetSocketAddress> getServers() {
        return SERVER_REGISTRY;
    }

}
