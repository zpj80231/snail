package com.sanil.source.code.rpc.server;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.sanil.source.code.rpc.common.config.RpcConfig;
import com.sanil.source.code.rpc.common.exception.RpcException;
import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;
import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.server.handler.RpcServerInitializer;
import com.sanil.source.code.rpc.server.registry.LocalServerRegistry;
import com.sanil.source.code.rpc.server.registry.LocalServiceRegistry;
import com.sanil.source.code.rpc.server.registry.ServerRegistry;
import com.sanil.source.code.rpc.server.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

/**
 * RPC 服务器管理器
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
@Getter
public class RpcServerManager {

    private final String host;
    private final int port;
    private final ServerRegistry serverRegistry;
    private final ServiceRegistry serviceRegistry;

    public RpcServerManager() {
        this(RpcConfig.getServerHost(), RpcConfig.getServerPort());
    }

    public RpcServerManager(String host, int port) {
        this(host, port, new LocalServerRegistry(), new LocalServiceRegistry());
    }

    public RpcServerManager(String host, int port, ServerRegistry serverRegistry, ServiceRegistry serviceRegistry) {
        this.host = host;
        this.port = port;
        this.serverRegistry = serverRegistry;
        this.serviceRegistry = serviceRegistry;
        autoRegister();
    }

    /**
     * rpc服务端启动
     */
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new RpcServerInitializer(this));
        try {
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("rpc server 启动成功，监听地址: {}:{}", host, port);
            channel.closeFuture().sync().addListener(future -> {
                serviceRegistry.getServices().keySet().parallelStream().forEach(serviceRegistry::unregister);
                serverRegistry.getServers().keySet().parallelStream().forEach(serverRegistry::unregister);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("rpc server 启动失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 自动注册相关 service提供者
     */
    private void autoRegister() {
        // 根据 EnableRpcServer 确定扫描范围（为空则从main类开始扫描），扫描指定包下的类，并完成注册
        String mainClassPath = findMainClassPath();
        Class<?> mainClass = ClassUtil.loadClass(mainClassPath);
        EnableRpcServer enableRpcServer = AnnotationUtil.getAnnotation(mainClass, EnableRpcServer.class);
        if (enableRpcServer == null) {
            throw new RpcException("启动类未添加 @EnableRpcServer 注解");
        }
        String scanPackage = Optional.ofNullable(enableRpcServer.scan())
                .filter(StrUtil::isNotBlank)
                .orElse(mainClassPath.substring(0, mainClassPath.lastIndexOf(".")));
        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(scanPackage, RpcService.class);
        for (Class<?> aClass : classSet) {
            if (ArrayUtil.isEmpty(aClass.getInterfaces())) {
                continue;
            }
            String serviceName = Optional.ofNullable(aClass.getAnnotation(RpcService.class).name())
                    .filter(StrUtil::isNotBlank)
                    .orElse(aClass.getInterfaces()[0].getName());
            Object service = ReflectUtil.newInstance(aClass);
            doRegister(serviceName, service);
        }

    }

    /**
     * 注册
     *
     * @param serviceName 服务名称
     * @param service     服务
     */
    private void doRegister(String serviceName, Object service) {
        if (log.isDebugEnabled()) {
            log.debug("register serviceName: {}, service: {}, host: {}, port: {}", serviceName, service, host, port);
        }
        serviceRegistry.register(serviceName, service);
        serverRegistry.register(serviceName, new InetSocketAddress(host, port));
    }

    /**
     * 查找主类路径
     *
     * @return {@link String }
     */
    private static String findMainClassPath() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }

}

