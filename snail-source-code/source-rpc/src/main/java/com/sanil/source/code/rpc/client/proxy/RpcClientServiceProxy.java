package com.sanil.source.code.rpc.client.proxy;

import cn.hutool.core.util.IdUtil;
import com.sanil.source.code.rpc.client.domain.RpcClientChannel;
import com.sanil.source.code.rpc.client.util.PromiseManager;
import com.sanil.source.code.rpc.common.exception.RpcException;
import com.sanil.source.code.rpc.common.message.RequestMessage;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.Proxy;

/**
 * RPC 客户端服务代理
 *
 * @author zhangpengjun
 * @date 2025/5/8
 */
public class RpcClientServiceProxy {
    
    private final RpcClientChannel channel;

    public RpcClientServiceProxy(RpcClientChannel channel) {
        this.channel = channel;
    }

    /**
     * 创建代理
     *
     * @param serviceClass 被代理的类
     * @return {@link T }
     */
    public <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader classLoader = serviceClass.getClassLoader();
        Class<?>[] interfaces = serviceClass.getInterfaces();
        Object result = Proxy.newProxyInstance(classLoader, interfaces, ((proxy, method, args) -> {
            long sequenceId = IdUtil.getSnowflakeNextId();
            DefaultPromise<Object> promise = new DefaultPromise<Object>() {};
            PromiseManager.add(sequenceId, promise);

            // 构造请求消息，进行远程调用
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setSequenceId(sequenceId);
            requestMessage.setInterfaceName(serviceClass.getName());
            requestMessage.setMethodName(method.getName());
            requestMessage.setParameterTypes(method.getParameterTypes());
            requestMessage.setParameterValues(args);
            requestMessage.setReturnType(method.getReturnType());
            channel.sendMessage(requestMessage);

            // 等待远程调用结果
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            } else {
                throw new RpcException(promise.cause());
            }
        }));
        return (T) result;
    }

}
