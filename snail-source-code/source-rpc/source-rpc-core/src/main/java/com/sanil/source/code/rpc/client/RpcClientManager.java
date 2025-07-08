package com.sanil.source.code.rpc.client;

import cn.hutool.core.collection.CollUtil;
import com.sanil.source.code.rpc.client.handler.RpcClientInitializer;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.extension.ExtensionLoader;
import com.sanil.source.code.rpc.core.loadbalance.ServerDiscovery;
import com.sanil.source.code.rpc.core.message.RequestMessage;
import com.sanil.source.code.rpc.core.util.ChannelManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * RPC 客户端管理器
 *
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
@Getter
public class RpcClientManager {

    private static final RpcConfig rpcConfig = RpcConfig.loadFromFile();
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private final ServerDiscovery serverDiscovery;

    public RpcClientManager() {
        this.serverDiscovery = ExtensionLoader.getExtensionLoader(ServerDiscovery.class).getExtension(rpcConfig.getDiscovery());
        initBootStrap();
    }

    private void initBootStrap() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new RpcClientInitializer(rpcConfig));
    }

    /**
     * 连接
     *
     * @return {@link Channel }
     */
    public Channel connect() {
        return connect(rpcConfig.getServerHost(), rpcConfig.getServerPort());
    }

    /**
     * 连接
     *
     * @param host 主机
     * @param port 端口
     * @return {@link Channel }
     */
    public Channel connect(String host, int port) {
        return connect(InetSocketAddress.createUnresolved(host, port));
    }

    /**
     * 连接
     *
     * @param socketAddress socket 地址
     * @return {@link Channel }
     */
    public Channel connect(InetSocketAddress socketAddress) {
        // 连接复用
        Channel channel = ChannelManager.get(socketAddress.toString());
        if (channel != null && channel.isOpen() && channel.isActive()) {
            return channel;
        } else {
            ChannelManager.removeAndClose(socketAddress.toString());
        }

        // 创建连接
        try {
            channel = bootstrap.connect(socketAddress).sync().channel();
            channel.closeFuture().addListener(future -> ChannelManager.removeAndClose(socketAddress.toString()));
            log.info("rpc client 连接成功 -> {}", socketAddress);
            ChannelManager.add(socketAddress.toString(), channel);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RpcException("连接被中断", e);
        } catch (Exception e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RpcException("rpc client 启动失败 -> " + cause.getMessage(), e);
        }

        return channel;
    }

    /**
     * 发送 RPC 请求
     *
     * @param requestMessage 请求消息
     */
    public void sendRpcRequest(RequestMessage requestMessage) {
        InetSocketAddress socketAddress = serverDiscovery.lookup(requestMessage.getRpcServiceName());
        Channel channel = connect(socketAddress);
        if (!channel.isOpen() || !channel.isActive()) {
            ChannelManager.removeAndClose(socketAddress.toString());
            return;
        }
        channel.writeAndFlush(requestMessage);
    }

    /**
     * 关闭
     */
    public void shutdown() {
        if (CollUtil.isEmpty(ChannelManager.getChannels())) {
            group.shutdownGracefully();
        }
    }

}
