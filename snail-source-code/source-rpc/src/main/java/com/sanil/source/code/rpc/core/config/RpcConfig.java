package com.sanil.source.code.rpc.core.config;

import com.sanil.source.code.rpc.core.exception.RpcException;
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
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RpcConfig {

    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 8023;
    static Properties properties;

    static {
        try (InputStream inputStream = RpcConfig.class.getResourceAsStream("/rpc.properties")) {
            properties = new Properties();
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RpcException("初始化配置文件异常", e);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getServerPort() {
        return Integer.parseInt(getProperty("rpc.server.port", String.valueOf(SERVER_PORT)));
    }

    public static String getServerHost() {
        return getProperty("rpc.server.host", SERVER_HOST);
    }

    public static byte getSerializer() {
        return Byte.parseByte(getProperty("rpc.serializer", "1"));
    }

    public static String getLoadBalance() {
        return getProperty("rpc.client.loadbalance", "roundRobin");
    }

    public static String getServerRegistry() {
        return getProperty("rpc.registry", "local");
    }

    public static String getDiscovery() {
        return getProperty("rpc.discovery", "default");
    }

    public static byte getCompress() {
        return Byte.parseByte(getProperty("rpc.compress", "1"));
    }

}
