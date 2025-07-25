package com.sanil.source.code.rpc.core.registry;

import com.sanil.source.code.rpc.core.extension.SPI;

import java.util.Map;

/**
 * 服务注册中心：维护服务和具体的服务实例之间的映射关系
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@SPI
public interface ServiceProvider {

    /**
     * 注册
     *
     * @param serviceName 服务名称
     * @param service     服务
     */
    void register(String serviceName, Object service);

    /**
     * 取消注册
     *
     * @param serviceName 服务名称
     * @return
     */
    Object unregister(String serviceName);

    /**
     * 获取服务器地址
     *
     * @param serviceName 服务名称
     * @return {@link Object }
     */
    Object getService(String serviceName);

    /**
     * 获取所有服务
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    Map<String, Object> getServices();

}
