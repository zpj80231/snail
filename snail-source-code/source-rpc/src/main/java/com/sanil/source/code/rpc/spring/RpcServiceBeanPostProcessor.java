package com.sanil.source.code.rpc.spring;

import com.sanil.source.code.rpc.client.RpcClientManager;
import com.sanil.source.code.rpc.client.annotation.RpcReference;
import com.sanil.source.code.rpc.client.proxy.RpcClientProxy;
import com.sanil.source.code.rpc.client.proxy.RpcReferenceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * RpcReference 代理
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Slf4j
@Component
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {

    private final RpcClientManager rpcClientManager;

    public RpcServiceBeanPostProcessor() {
        rpcClientManager = new RpcClientManager();
    }

    /**
     * rpc 客户端，为 @RpcReference 创建代理
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(RpcReference.class)) {
                // 获取 @RpcReference 注解
                RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                String group = rpcReference.group();
                String version = rpcReference.version();
                // 创建代理
                RpcReferenceBuilder rpcReferenceBuilder = RpcReferenceBuilder.builder()
                        .group(group).version(version).service(field.getType()).build();
                RpcClientProxy proxyRpcReference = new RpcClientProxy(rpcClientManager, rpcReferenceBuilder);
                Object proxyService = proxyRpcReference.getProxyService(field.getType());
                // 注入代理
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, proxyService);
            }
        }
        return bean;
    }

}
