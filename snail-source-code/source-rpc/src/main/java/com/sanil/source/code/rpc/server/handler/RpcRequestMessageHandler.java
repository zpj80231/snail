package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.message.RequestMessage;
import com.sanil.source.code.rpc.core.message.ResponseMessage;
import com.sanil.source.code.rpc.core.registry.ServiceProvider;
import com.sanil.source.code.rpc.core.util.NettyAttrUtil;
import com.sanil.source.code.rpc.server.RpcServerManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) {
        // 构造响应消息
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setSequenceId(msg.getSequenceId());

        // 获取service实现类，本地调用
        RpcServerManager manager = NettyAttrUtil.getManager(ctx.channel());
        ServiceProvider serviceProvider = manager.getServiceProvider();
        Object service = serviceProvider.getService(msg.getRpcServiceName());
        if (service == null) {
            String message = "服务未找到：" + msg.getRpcServiceName();
            log.error(message);
            responseMessage.setExceptionValue(new RpcException(message));
            ctx.writeAndFlush(responseMessage);
            return;
        }

        // 调用方法
        Method method;
        try {
            method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object result = method.invoke(service, msg.getParameterValues());
            responseMessage.setReturnValue(result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("远程调用异常", e);
            responseMessage.setExceptionValue(new RpcException("远程调用异常：" + e.getCause().getMessage()));
        }

        ctx.writeAndFlush(responseMessage);
    }

}
