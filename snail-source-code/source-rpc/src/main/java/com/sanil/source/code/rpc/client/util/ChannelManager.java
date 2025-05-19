package com.sanil.source.code.rpc.client.util;

import io.netty.channel.Channel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ChannelManager {

    private static final Map<String, Channel> CHANNELS = new ConcurrentHashMap<>();

    public static void add(String key, Channel channel) {
        CHANNELS.put(key, channel);
    }

    public static Channel get(String key) {
        return CHANNELS.get(key);
    }

    public static Map<String, Channel> getChannels() {
        return CHANNELS;
    }

    public static void remove(String key) {
        CHANNELS.remove(key);
    }

}
