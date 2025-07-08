package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.core.message.PingMessage;
import com.sanil.source.code.rpc.core.message.PongMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
@ChannelHandler.Sharable
public class PingMessageHandler extends SimpleChannelInboundHandler<PingMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
        // log.debug("收到客户端心跳消息:{}", msg.getMessageType());
        PongMessage pongMessage = new PongMessage();
        pongMessage.setSequenceId(msg.getSequenceId());
        ctx.writeAndFlush(pongMessage);
    }

}
