package com.sanil.source.code.rpc.client.handler;

import com.sanil.source.code.rpc.core.message.PongMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
@Slf4j
@ChannelHandler.Sharable
public class PongMessageHandler extends SimpleChannelInboundHandler<PongMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongMessage msg) throws Exception {

    }

}
