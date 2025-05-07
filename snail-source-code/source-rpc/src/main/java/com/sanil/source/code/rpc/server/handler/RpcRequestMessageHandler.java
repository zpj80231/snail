package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.common.message.RequestMessage;
import com.sanil.source.code.rpc.common.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        // 构造响应消息
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setSequenceId(msg.getSequenceId());

        // 获取服务，本地调用
    }

}
