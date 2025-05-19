package com.sanil.source.code.rpc.core.registry;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
public class LocalServiceRegistry implements ServiceRegistry {

    private static final Map<String, Object> SERVICE_REGISTRY = new HashMap<>();

    @Override
    public void register(String serviceName, Object service) {
        SERVICE_REGISTRY.put(serviceName, service);
    }

    @Override
    public Object unregister(String serviceName) {
        return SERVICE_REGISTRY.remove(serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        return SERVICE_REGISTRY.get(serviceName);
    }

    @Override
    public Map<String, Object> getServices() {
        return SERVICE_REGISTRY;
    }

}
