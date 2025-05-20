package com.sanil.source.code.rpc.core.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    InetSocketAddress doSelect(List<InetSocketAddress> servers) {
        int current;
        int next;
        do {
            current = index.get();
            next = current == Integer.MAX_VALUE ? 0 : current + 1;
        } while (!index.compareAndSet(current, next));
        int pos = current % servers.size();
        return servers.get(pos);
    }

}
