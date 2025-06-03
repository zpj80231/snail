package com.sanil.source.code.rpc.core.config;

import com.sanil.source.code.rpc.core.exception.RpcException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * RPC 配置
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
@Data
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RpcConfig {

    private static final String path = "/rpc.properties";
    private String serverHost = "127.0.0.1";
    private int serverPort = 8023;
    private String serializer = "json";
    private String loadBalance = "roundRobin";
    private String serverRegistry = "local";
    private String serviceProvider = "local";
    private String discovery = "default";
    private byte compress = 1;

    public static RpcConfig loadFromFile() {
        Properties props = new Properties();
        try (InputStream inputStream = RpcConfig.class.getResourceAsStream(path)) {
            if (inputStream != null) {
                props.load(inputStream);
            }
        } catch (IOException e) {
            throw new RpcException("初始化配置文件异常", e);
        }

        RpcConfig config = new RpcConfig();
        config.setServerHost(props.getProperty("rpc.server.host", config.getServerHost()));
        config.setServerPort(Integer.parseInt(props.getProperty("rpc.server.port", String.valueOf(config.getServerPort()))));
        config.setSerializer(props.getProperty("rpc.serializer", String.valueOf(config.getSerializer())));
        config.setLoadBalance(props.getProperty("rpc.client.loadbalance", config.getLoadBalance()));
        config.setServerRegistry(props.getProperty("rpc.registry.server", config.getServerRegistry()));
        config.setServerRegistry(props.getProperty("rpc.registry.provider", config.getServerRegistry()));
        config.setDiscovery(props.getProperty("rpc.discovery", config.getDiscovery()));
        config.setCompress(Byte.parseByte(props.getProperty("rpc.compress", String.valueOf(config.getCompress()))));

        return config;
    }

}
