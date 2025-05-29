package com.sanil.source.code.rpc.server;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.extension.ExtensionLoader;
import com.sanil.source.code.rpc.core.registry.LocalServiceRegistry;
import com.sanil.source.code.rpc.core.registry.ServerRegistry;
import com.sanil.source.code.rpc.core.registry.ServiceRegistry;
import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;
import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.server.handler.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;
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

    private static final RpcConfig rpcConfig = RpcConfig.loadFromFile();
    private final InetSocketAddress serverAddress;
    private final ServerRegistry serverRegistry;
    private final ServiceRegistry serviceRegistry;

    public RpcServerManager() {
        this(rpcConfig.getServerPort());
    }

    public RpcServerManager(int port) {
        this(new InetSocketAddress(rpcConfig.getServerHost(), port));
    }

    public RpcServerManager(String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    public RpcServerManager(InetSocketAddress serverAddress) {
        this(serverAddress,
                ExtensionLoader.getExtensionLoader(ServerRegistry.class).getExtension(rpcConfig.getServerRegistry()),
                new LocalServiceRegistry());
    }

    public RpcServerManager(InetSocketAddress serverAddress, ServerRegistry serverRegistry, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
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
                .childHandler(new RpcServerInitializer(rpcConfig, this));
        try {
            Channel channel = bootstrap.bind(serverAddress.getPort()).sync().channel();
            log.info("rpc server 启动成功，监听地址: {}", serverAddress);
            channel.closeFuture().sync().addListener(future -> destroy());
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
            mainClassPath = findEnableRpcServerClassPath();
            mainClass = ClassUtil.loadClass(mainClassPath);
            enableRpcServer = AnnotationUtil.getAnnotation(mainClass, EnableRpcServer.class);
            if (enableRpcServer == null) {
                throw new RpcException("启动类未添加或未找到 @EnableRpcServer 注解");
            }
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
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    /**
     * 注册
     *
     * @param serviceName 服务名称
     * @param service     服务
     */
    private void doRegister(String serviceName, Object service) {
        if (log.isDebugEnabled()) {
            log.debug("register serviceName: {}, service: {}, serverAddress: {}", serviceName, service, serverAddress);
        }
        serviceRegistry.register(serviceName, service);
        serverRegistry.register(serviceName, serverAddress);
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

    /**
     * 查找 EnableRpcServer 类路径
     *
     * @return {@link String }
     */
    private static String findEnableRpcServerClassPath() {
        Set<Class<?>> classes = ClassUtil.scanPackage("");
        for (Class<?> clazz : classes) {
            if (Modifier.isPublic(clazz.getModifiers()) && Modifier.isStatic(clazz.getModifiers())) {
                continue;
            }
            EnableRpcServer annotation = AnnotationUtil.getAnnotation(clazz, EnableRpcServer.class);
            if (annotation != null) {
                return clazz.getName();
            }
        }
        return "";
    }

    /**
     * 销毁资源
     */
    private void destroy() {
        serviceRegistry.getServices().keySet().parallelStream().forEach(serviceName -> {
            serviceRegistry.unregister(serviceName);
            serverRegistry.unregister(serviceName, serverAddress);
        });
    }

}

