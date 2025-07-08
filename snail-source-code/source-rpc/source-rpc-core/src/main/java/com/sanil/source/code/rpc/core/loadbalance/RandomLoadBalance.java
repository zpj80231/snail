package com.sanil.source.code.rpc.core.loadbalance;

import cn.hutool.core.util.RandomUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    InetSocketAddress doSelect(List<InetSocketAddress> servers) {
        return RandomUtil.randomEle(servers);
    }

}
