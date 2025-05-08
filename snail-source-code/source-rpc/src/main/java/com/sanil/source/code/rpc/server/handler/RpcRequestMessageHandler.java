package com.sanil.source.code.rpc.server.handler;

import com.sanil.source.code.rpc.common.exception.RpcException;
import com.sanil.source.code.rpc.common.message.RequestMessage;
import com.sanil.source.code.rpc.common.message.ResponseMessage;
import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.server.registry.ServiceRegistry;
import com.sanil.source.code.rpc.server.util.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
        ServiceRegistry serviceRegistry = manager.getServiceRegistry();
        Object service = serviceRegistry.getService(msg.getInterfaceName());
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.warn("channel:{} 长时间未收到心跳包，断开连接", ctx.channel());
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
