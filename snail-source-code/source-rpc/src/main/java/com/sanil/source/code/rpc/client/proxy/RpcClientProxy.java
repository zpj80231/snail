package com.sanil.source.code.rpc.client.proxy;

import cn.hutool.core.util.IdUtil;
import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.util.PromiseManager;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.message.RequestMessage;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * RPC 客户端服务代理
 *
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    
    private final RpcClientManager manager;

    public RpcClientProxy(RpcClientManager manager) {
        this.manager = manager;
    }

    /**
     * 创建代理
     *
     * @param interfaceClazz 被代理的类
     * @return {@link T }
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyService(Class<T> interfaceClazz) {
        ClassLoader classLoader = interfaceClazz.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long sequenceId = IdUtil.getSnowflakeNextId();
        DefaultPromise<Object> promise = new DefaultPromise<Object>() {};
        PromiseManager.add(sequenceId, promise);

        // 构造请求消息，进行远程调用
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setSequenceId(sequenceId);
        requestMessage.setInterfaceName(method.getDeclaringClass().getName());
        requestMessage.setMethodName(method.getName());
        requestMessage.setParameterTypes(method.getParameterTypes());
        requestMessage.setParameterValues(args);
        requestMessage.setReturnType(method.getReturnType());

        InetSocketAddress socketAddress = manager.getServerDiscovery().lookup(requestMessage.getInterfaceName());
        RpcClientChannel channel = manager.connect(socketAddress);
        channel.sendMessage(requestMessage);

        // 等待远程调用结果
        promise.await();
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            throw new RpcException(promise.cause());
        }
    }

}
