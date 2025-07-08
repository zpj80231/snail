package com.sanil.source.code.rpc.core.loadbalance;

import com.sun.istack.internal.NotNull;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public InetSocketAddress select(@NotNull List<InetSocketAddress> servers) {
        if (servers.size() == 1) {
            return servers.get(0);
        }
        return doSelect(servers);
    }

    abstract InetSocketAddress doSelect(List<InetSocketAddress> servers);

}
