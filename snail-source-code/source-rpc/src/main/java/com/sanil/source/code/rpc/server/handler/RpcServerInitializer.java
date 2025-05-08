package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.common.codec.MessageCodec;
import com.sanil.source.code.rpc.common.codec.ProtocolFrameDecoder;
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

    public static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    public static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    public static final HeartBeatServerHandler HEART_BEAT_SERVER_HANDLER = new HeartBeatServerHandler();
    public static final PingMessageHandler PING_MESSAGE_HANDLER = new PingMessageHandler();
    public static final RpcRequestMessageHandler RPC_REQUEST_MESSAGE_HANDLER = new RpcRequestMessageHandler();

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        pipeline.addLast(new ProtocolFrameDecoder());
        pipeline.addLast(MESSAGE_CODEC);
        pipeline.addLast(LOGGING_HANDLER);
        pipeline.addLast(HEART_BEAT_SERVER_HANDLER);
        pipeline.addLast(PING_MESSAGE_HANDLER);
        pipeline.addLast(RPC_REQUEST_MESSAGE_HANDLER);
    }

}
