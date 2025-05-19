package com.sanil.source.code.rpc.client.handler;

import com.sanil.source.code.rpc.client.util.PromiseManager;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        // 获取远程调用结果
        long sequenceId = msg.getSequenceId();
        Object returnValue = msg.getReturnValue();
        Throwable exceptionValue = msg.getExceptionValue();
        Promise<Object> promise = PromiseManager.remove(sequenceId);
        if (promise == null) {
            throw new RpcException("未找到对应的promise, sequenceId: " + sequenceId);
        }
        if (exceptionValue != null) {
            promise.setFailure(exceptionValue);
        }
        promise.setSuccess(returnValue);
    }

}
