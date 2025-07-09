package com.sanil.source.code.rpc.server;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.extension.ExtensionLoader;
import com.sanil.source.code.rpc.core.registry.ServerRegistry;
import com.sanil.source.code.rpc.core.registry.ServiceProvider;
import com.sanil.source.code.rpc.core.util.RpcServiceUtil;
import com.sanil.source.code.rpc.server.annotation.EnableRpcServer;
import com.sanil.source.code.rpc.server.annotation.RpcService;
import com.sanil.source.code.rpc.server.handler.RpcServerInitializer;
import com.sanil.source.code.rpc.spring.EnableRpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.HashSet;
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

    private static final RpcConfig RPC_CONFIG = RpcConfig.loadFromFile();
    private final InetSocketAddress serverAddress;
    private final ServerRegistry serverRegistry;
    private final ServiceProvider serviceProvider;

    public RpcServerManager() {
        this(RPC_CONFIG.getServerPort());
    }

    public RpcServerManager(int port) {
        this(new InetSocketAddress(RPC_CONFIG.getServerHost(), port));
    }

    public RpcServerManager(String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    public RpcServerManager(InetSocketAddress serverAddress) {
        this(serverAddress,
                ExtensionLoader.getExtensionLoader(ServerRegistry.class).getExtension(RPC_CONFIG.getServerRegistry()),
                ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension(RPC_CONFIG.getServiceProvider()));
    }

    public RpcServerManager(InetSocketAddress serverAddress, ServerRegistry serverRegistry, ServiceProvider serviceProvider) {
        this.serverAddress = serverAddress;
        this.serverRegistry = serverRegistry;
        this.serviceProvider = serviceProvider;
        autoRegister();
    }

    /**
     * rpc服务端启动，同步
     */
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new RpcServerInitializer(RPC_CONFIG, this));
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
     * rpc服务端启动，异步
     */
    public void startAsync() {
        ThreadUtil.execAsync(this::start);
    }


    /**
     * 自动注册相关提供者
     */
    private void autoRegister() {
        // 根据 Enable注解 确定扫描范围（为空则从main类开始扫描），扫描指定包下的类，并完成注册
        String mainClassPath = findMainClassPath();
        Class<?> mainClass = ClassUtil.loadClass(mainClassPath);
        EnableRpcService enableRpcService = AnnotationUtil.getAnnotation(mainClass, EnableRpcService.class);
        EnableRpcServer enableRpcServer = AnnotationUtil.getAnnotation(mainClass, EnableRpcServer.class);
        if (enableRpcService == null && enableRpcServer == null) {
            mainClassPath = findAnnotationClassPath(EnableRpcService.class);
            mainClass = ClassUtil.loadClass(mainClassPath);
            enableRpcService = AnnotationUtil.getAnnotation(mainClass, EnableRpcService.class);
        }
        if (enableRpcService == null && enableRpcServer == null) {
            mainClassPath = findAnnotationClassPath(EnableRpcServer.class);
            mainClass = ClassUtil.loadClass(mainClassPath);
            enableRpcServer = AnnotationUtil.getAnnotation(mainClass, EnableRpcServer.class);
            if (enableRpcServer == null) {
                throw new RpcException("启动类未添加或未找到 @EnableRpcService 或 @EnableRpcServer 注解");
            }
        }

        // 处理多个包路径
        Set<Class<?>> classSet = new HashSet<>();
        String[] basePackages = enableRpcService != null ? enableRpcService.value() : enableRpcServer.value();
        if (ArrayUtil.isEmpty(basePackages) || (basePackages.length == 1 && StrUtil.isBlank(basePackages[0]))) {
            // 没有指定包路径，使用默认包路径
            String defaultPackage = mainClassPath.substring(0, mainClassPath.lastIndexOf("."));
            classSet.addAll(ClassUtil.scanPackageByAnnotation(defaultPackage, RpcService.class));
        } else {
            // 扫描所有指定的包路径
            for (String basePackage : basePackages) {
                if (StrUtil.isNotBlank(basePackage)) {
                    classSet.addAll(ClassUtil.scanPackageByAnnotation(basePackage, RpcService.class));
                }
            }
        }
        for (Class<?> aClass : classSet) {
            // 检查类是否为接口（如果是接口则跳过，只扫描实现类）
            if (ArrayUtil.isEmpty(aClass.getInterfaces()) || aClass.isInterface() || Modifier.isAbstract(aClass.getModifiers())) {
                continue;
            }
            RpcService rpcServiceAnnotation = aClass.getAnnotation(RpcService.class);
            String serviceName = Optional.ofNullable(rpcServiceAnnotation.name())
                    .filter(StrUtil::isNotBlank).orElse(aClass.getInterfaces()[0].getName());
            String group = rpcServiceAnnotation.group();
            String version = rpcServiceAnnotation.version();
            Object service = ReflectUtil.newInstance(aClass);
            doRegister(RpcServiceUtil.getProviderName(serviceName, group, version), service);
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
        serviceProvider.register(serviceName, service);
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
     * 查找 指定注解 类路径
     *
     * @return {@link String }
     */
    private static <A extends Annotation> String findAnnotationClassPath(Class<A> annotationType) {
        Set<Class<?>> classes = ClassUtil.scanPackage("");
        for (Class<?> clazz : classes) {
            if (Modifier.isPublic(clazz.getModifiers()) && Modifier.isStatic(clazz.getModifiers())) {
                continue;
            }
            Annotation annotation = AnnotationUtil.getAnnotation(clazz, annotationType);
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
        serviceProvider.getServices().keySet().parallelStream().forEach(serviceProvider::unregister);
        serverRegistry.getServers().keySet().parallelStream().forEach(serviceName -> serverRegistry.unregister(serviceName, serverAddress));
    }

}

