package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.core.codec.MessageCodec;
import com.sanil.source.code.rpc.core.codec.ProtocolFrameDecoder;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.util.NettyAttrUtil;
import com.sanil.source.code.rpc.server.RpcServerManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * RPC 服务器初始化器
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class RpcServerInitializer extends ChannelInitializer<Channel> {

    private final RpcConfig rpcConfig;
    private final RpcServerManager rpcServerManager;

    public static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    public static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    public static final HeartBeatServerHandler HEART_BEAT_SERVER_HANDLER = new HeartBeatServerHandler();
    public static final PingMessageHandler PING_MESSAGE_HANDLER = new PingMessageHandler();
    public static final RpcRequestMessageHandler RPC_REQUEST_MESSAGE_HANDLER = new RpcRequestMessageHandler();

    public RpcServerInitializer(RpcConfig rpcConfig, RpcServerManager rpcServerManager) {
        this.rpcConfig = rpcConfig;
        this.rpcServerManager = rpcServerManager;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        NettyAttrUtil.setManager(channel, rpcServerManager);
        NettyAttrUtil.setRpcConfig(channel, rpcConfig);
        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        pipeline.addLast(new ProtocolFrameDecoder());
        pipeline.addLast(MESSAGE_CODEC);
        // pipeline.addLast(LOGGING_HANDLER);
        pipeline.addLast(HEART_BEAT_SERVER_HANDLER);
        pipeline.addLast(PING_MESSAGE_HANDLER);
        pipeline.addLast(RPC_REQUEST_MESSAGE_HANDLER);
    }

}
