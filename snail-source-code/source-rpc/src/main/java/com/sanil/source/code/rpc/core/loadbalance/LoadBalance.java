package com.sanil.source.code.rpc.core.loadbalance;

import com.sanil.source.code.rpc.core.extension.SPI;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
@SPI
public interface LoadBalance {

    /**
     * 选择
     *
     * @param servers 服务器
     * @return {@link InetSocketAddress }
     */
    InetSocketAddress select(List<InetSocketAddress> servers);

}
