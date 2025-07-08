package com.sanil.source.code.rpc.core.registry;

import com.sanil.source.code.rpc.core.extension.SPI;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

/**
 * 服务端注册表：记录服务在哪个server服务器地址的对应关系
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@SPI
public interface ServerRegistry {

    /**
     * 注册
     *
     * @param serviceName   服务名称
     * @param serverAddress 服务器地址
     */
    void register(String serviceName, InetSocketAddress serverAddress);

    /**
     * 取消注册
     *
     * @param serviceName 服务名称
     * @return
     */
    InetSocketAddress unregister(String serviceName, InetSocketAddress serverAddress);

    /**
     * 获取服务器地址
     *
     * @param serviceName 服务名称
     * @return {@link InetSocketAddress }
     */
    Set<InetSocketAddress> getServerAddress(String serviceName);

    /**
     * 获取所有服务器
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    Map<String, Set<InetSocketAddress>> getServers();

}
