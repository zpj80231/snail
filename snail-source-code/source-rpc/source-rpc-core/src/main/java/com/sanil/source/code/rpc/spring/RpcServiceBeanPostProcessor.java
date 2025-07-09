package com.sanil.source.code.rpc.spring;

import cn.hutool.core.util.StrUtil;
import com.sanil.source.code.rpc.core.util.RpcServiceUtil;
import com.sanil.source.code.rpc.server.RpcServerManager;
import com.sanil.source.code.rpc.server.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * Spring RpcService Bean 注册
 *
 * @author zhangpj
 * @date 2025/7/9
 */
@Slf4j
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {

    private final RpcServerManager rpcServerManager;

    public RpcServiceBeanPostProcessor(RpcServerManager rpcServerManager) {
        this.rpcServerManager = rpcServerManager;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.getTargetClass(bean);
        RpcService rpcService = AnnotationUtils.findAnnotation(clazz, RpcService.class);
        if (rpcService != null) {
            // 获取服务名、分组、版本
            String serviceName = Optional.ofNullable(rpcService.name())
                    .filter(StrUtil::isNotBlank)
                    .orElse(clazz.getInterfaces()[0].getName());
            String group = rpcService.group();
            String version = rpcService.version();
            // 注册服务
            String providerName = RpcServiceUtil.getProviderName(serviceName, group, version);
            InetSocketAddress serverAddress = rpcServerManager.getServerAddress();
            log.debug("register RpcService: {}, service: {}, serverAddress: {}", serviceName, bean, serverAddress);
            rpcServerManager.getServiceProvider().register(providerName, bean);
            rpcServerManager.getServerRegistry().register(providerName, serverAddress);
        }
        return bean;
    }

}
