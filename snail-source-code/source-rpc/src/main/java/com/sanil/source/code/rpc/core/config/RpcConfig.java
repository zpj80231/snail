package com.sanil.source.code.rpc.core.config;

import com.sanil.source.code.rpc.core.exception.RpcException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * RPC 配置
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
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
            throw new RpcException(e);
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

    public static String getSerializer() {
        return getProperty("rpc.serializer", "json");
    }

    public static String getLoadBalance() {
        return getProperty("rpc.client.loadbalance", "roundRobin");
    }

}
