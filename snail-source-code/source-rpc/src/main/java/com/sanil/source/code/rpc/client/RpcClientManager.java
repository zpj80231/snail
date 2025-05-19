package com.sanil.source.code.rpc.client;

import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.handler.RpcClientInitializer;
import com.sanil.source.code.rpc.common.config.RpcConfig;
import com.sanil.source.code.rpc.common.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClientManager {

    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private Map<String, Channel> channels;

    public RpcClientManager() {
        initBootStrap();
    }

    private void initBootStrap() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        channels = new ConcurrentHashMap<>();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new RpcClientInitializer());
    }

    public RpcClientChannel connect() {
        return connect(RpcConfig.getServerHost(), RpcConfig.getServerPort());
    }

    public RpcClientChannel connect(String host, int port) {
        Channel channel;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
            channels.put(channel.id().asLongText(), channel);
            channel.closeFuture().addListener(future -> log.debug("client closed"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RpcException("rpc client 启动失败", e);
        }

        return new RpcClientChannel(channel);
    }

    public void shutdown() {
        channels.values().parallelStream().forEach(ChannelOutboundInvoker::close);
        group.shutdownGracefully();
    }

}
