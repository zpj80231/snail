package com.sanil.source.code.rpc.client.handler;

import com.sanil.source.code.rpc.common.codec.MessageCodec;
import com.sanil.source.code.rpc.common.codec.ProtocolFrameDecoder;
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

    public static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    public static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    public static final RpcResponseMessageHandler RPC_RESPONSE_MESSAGE_HANDLER = new RpcResponseMessageHandler();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        pipeline.addLast(new ProtocolFrameDecoder());
        pipeline.addLast(MESSAGE_CODEC);
        // pipeline.addLast(LOGGING_HANDLER);
        pipeline.addLast(RPC_RESPONSE_MESSAGE_HANDLER);
    }

}
