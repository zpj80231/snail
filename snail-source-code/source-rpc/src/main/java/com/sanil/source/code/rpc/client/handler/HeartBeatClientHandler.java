package com.sanil.source.code.rpc.client.handler;

import cn.hutool.core.util.IdUtil;
import com.sanil.source.code.rpc.core.message.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpj
 * @date 2025/5/19
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatClientHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                // log.debug("channel:{} 发送心跳包", ctx.channel());
                PingMessage pingMessage = new PingMessage();
                pingMessage.setSequenceId(IdUtil.getSnowflakeNextId());
                ctx.writeAndFlush(pingMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel:{} 已经断开", ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("channel:{} 发生异常", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }

}
