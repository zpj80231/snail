package com.sanil.source.code.rpc.client;

import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.handler.RpcClientInitializer;
import com.sanil.source.code.rpc.common.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClientManager {

    private static final NioEventLoopGroup group = new NioEventLoopGroup();
    private static final Bootstrap bootstrap = new Bootstrap();

    public RpcClientManager() {
        initBootStrap();
    }

    private void initBootStrap() {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new RpcClientInitializer());
    }

    public RpcClientChannel connect() {
        Channel channel;
        try {
            channel = bootstrap.connect("127.0.0.1", 8023).sync().channel();
            channel.closeFuture().addListener(future -> log.debug("client closed"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RpcException("rpc client 启动失败", e);
        }

        return new RpcClientChannel(channel);
    }

}
