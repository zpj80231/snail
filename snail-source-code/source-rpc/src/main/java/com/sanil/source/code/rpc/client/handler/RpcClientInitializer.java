package com.sanil.source.code.rpc.client.handler;

import com.sanil.source.code.rpc.core.codec.MessageCodec;
import com.sanil.source.code.rpc.core.codec.ProtocolFrameDecoder;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.util.NettyAttrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * RPC 客户端初始化器
 *
 * @author zhangpengjun
 * @date 2025/5/8
 */
@ChannelHandler.Sharable
public class RpcClientInitializer extends ChannelInitializer<Channel> {

    private final RpcConfig rpcConfig;

    public static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    public static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    public static final HeartBeatClientHandler HEART_BEAT_CLIENT_HANDLER = new HeartBeatClientHandler();
    public static final PongMessageHandler PONG_MESSAGE_HANDLER = new PongMessageHandler();
    public static final RpcResponseMessageHandler RPC_RESPONSE_MESSAGE_HANDLER = new RpcResponseMessageHandler();

    public RpcClientInitializer(RpcConfig rpcConfig) {
        this.rpcConfig = rpcConfig;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        NettyAttrUtil.setRpcConfig(ch, rpcConfig);
        pipeline.addLast(new IdleStateHandler(0, 15, 0));
        pipeline.addLast(new ProtocolFrameDecoder());
        pipeline.addLast(MESSAGE_CODEC);
        // pipeline.addLast(LOGGING_HANDLER);
        pipeline.addLast(HEART_BEAT_CLIENT_HANDLER);
        pipeline.addLast(PONG_MESSAGE_HANDLER);
        pipeline.addLast(RPC_RESPONSE_MESSAGE_HANDLER);
    }

}
