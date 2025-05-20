package com.sanil.source.code.rpc.core.loadbalance;

import cn.hutool.core.collection.CollUtil;
import com.sanil.source.code.rpc.core.config.RpcConfig;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public InetSocketAddress select(List<InetSocketAddress> servers) {
        if (CollUtil.isEmpty(servers)) {
            return new InetSocketAddress(RpcConfig.getServerHost(), RpcConfig.getServerPort());
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        return doSelect(servers);
    }

    abstract InetSocketAddress doSelect(List<InetSocketAddress> servers);

}
