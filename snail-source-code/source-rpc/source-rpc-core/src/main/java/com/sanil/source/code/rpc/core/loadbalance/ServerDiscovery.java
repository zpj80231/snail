package com.sanil.source.code.rpc.core.loadbalance;

import com.sanil.source.code.rpc.core.extension.SPI;

import java.net.InetSocketAddress;

/**
 * 服务器发现
 *
 * @author zhangpj
 * @date 2025/5/19
 */
@SPI
public interface ServerDiscovery {

    /**
     * 查找一个服务节点
     *
     * @param serviceName 服务名称
     * @return {@link InetSocketAddress }
     */
    InetSocketAddress lookup(String serviceName);

}
