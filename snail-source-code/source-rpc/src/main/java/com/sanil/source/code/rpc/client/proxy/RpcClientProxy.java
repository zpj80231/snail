package com.sanil.source.code.rpc.client.proxy;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.annotation.RpcReference;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.message.RequestMessage;
import com.sanil.source.code.rpc.core.util.PromiseManager;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

/**
 * RPC 客户端服务代理
 *
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    
    private final RpcClientManager manager;
    private RpcReferenceBuilder rpcReferenceBuilder;

    public RpcClientProxy(RpcClientManager manager) {
        this.manager = manager;
    }

    public RpcClientProxy(RpcClientManager manager, RpcReferenceBuilder config) {
        this.manager = manager;
        this.rpcReferenceBuilder = config;
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
        RpcReference rpcReference = interfaceClazz.getAnnotation(RpcReference.class);
        if (rpcReference != null && rpcReferenceBuilder == null) {
            rpcReferenceBuilder = RpcReferenceBuilder.builder()
                    .group(rpcReference.group())
                    .version(rpcReference.version())
                    .service(interfaceClazz)
                    .build();
        }
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long sequenceId = IdUtil.getSnowflakeNextId();
        Promise<Object> promise = new DefaultPromise<>(manager.getGroup().next());
        PromiseManager.add(sequenceId, promise);

        // 构造请求消息，进行远程调用
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setSequenceId(sequenceId);
        requestMessage.setInterfaceName(method.getDeclaringClass().getName());
        requestMessage.setMethodName(method.getName());
        requestMessage.setParameterTypes(method.getParameterTypes());
        requestMessage.setParameterValues(args);
        requestMessage.setReturnType(method.getReturnType());
        if (rpcReferenceBuilder != null) {
            String serviceName = Optional.ofNullable(rpcReferenceBuilder.getService()).map(Class::getName).orElse(null);
            requestMessage.setInterfaceName(StrUtil.isNotBlank(serviceName) ? serviceName : requestMessage.getInterfaceName());
            requestMessage.setGroup(rpcReferenceBuilder.getGroup());
            requestMessage.setVersion(rpcReferenceBuilder.getVersion());
        }

        // 发起rpc请求
        manager.sendRpcRequest(requestMessage);

        // 等待远程调用结果
        promise.await(5000);
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            throw new RpcException("远程调用异常", promise.cause());
        }
    }

}
