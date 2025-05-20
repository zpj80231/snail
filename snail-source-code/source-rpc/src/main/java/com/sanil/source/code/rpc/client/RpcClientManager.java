package com.sanil.source.code.rpc.client;

import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.handler.RpcClientInitializer;
import com.sanil.source.code.rpc.client.util.ChannelManager;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.loadbalance.DefaultServerDiscovery;
import com.sanil.source.code.rpc.core.loadbalance.LoadBalance;
import com.sanil.source.code.rpc.core.loadbalance.RoundRobinLoadBalance;
import com.sanil.source.code.rpc.core.loadbalance.ServerDiscovery;
import com.sanil.source.code.rpc.core.registry.LocalServerRegistry;
import com.sanil.source.code.rpc.core.registry.ServerRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
@Getter
public class RpcClientManager {

    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private final LoadBalance loadBalance;
    private final ServerRegistry serverRegistry;
    private final ServerDiscovery serverDiscovery;

    public RpcClientManager() {
        this(new RoundRobinLoadBalance(), new LocalServerRegistry());
    }

    public RpcClientManager(LoadBalance loadBalance, ServerRegistry serverRegistry) {
        this.loadBalance = loadBalance;
        this.serverRegistry = serverRegistry;
        this.serverDiscovery = new DefaultServerDiscovery(loadBalance, serverRegistry);
        initBootStrap();
    }

    private void initBootStrap() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
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
        return connect(InetSocketAddress.createUnresolved(host, port));
    }

    public RpcClientChannel connect(SocketAddress socketAddress) {
        Channel channel;
        try {
            channel = bootstrap.connect(socketAddress).sync().channel();
            ChannelManager.add(channel.id().asLongText(), channel);
            channel.closeFuture();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RpcException("rpc client 启动失败", e);
        }

        return new RpcClientChannel(channel);
    }

    public void shutdown() {
        ChannelManager.getChannels().values().parallelStream().forEach(ChannelOutboundInvoker::close);
        group.shutdownGracefully();
    }

}
