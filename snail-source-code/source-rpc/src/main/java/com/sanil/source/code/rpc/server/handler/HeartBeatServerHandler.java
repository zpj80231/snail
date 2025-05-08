package com.sanil.source.code.rpc.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.warn("channel:{} 长时间未收到心跳包，断开连接", ctx.channel());
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

}
