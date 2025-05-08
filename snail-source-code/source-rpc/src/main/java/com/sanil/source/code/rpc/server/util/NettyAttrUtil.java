package com.sanil.source.code.rpc.server.util;

import com.sanil.source.code.rpc.server.RpcServerManager;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;

/**
 * Netty Attr Util
 *
 * @author zhangpj
 * @date 2025/02/11
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NettyAttrUtil {

    /**
     * 设置属性值
     *
     * @param channel Channel
     * @param key     AttributeKey
     * @param value   要设置的属性值
     * @param <T>     属性值的类型
     */
    public static <T> void setAttribute(Channel channel, AttributeKey<T> key, T value) {
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

    /**
     * 获取属性值
     *
     * @param channel Channel
     * @param key     AttributeKey
     * @param <T>     属性值的类型
     * @return 属性值，如果没有设置，返回 null
     */
    public static <T> T getAttribute(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }

    /**
     * 获取属性值，如果属性没有设置，返回默认值
     *
     * @param channel    Channel
     * @param key        AttributeKey
     * @param defaultVal 默认值
     * @param <T>        属性值的类型
     * @return 属性值，如果没有设置，返回默认值
     */
    public static <T> T getAttribute(Channel channel, AttributeKey<T> key, T defaultVal) {
        Attribute<T> attr = channel.attr(key);
        T value = attr.get();
        return value != null ? value : defaultVal;
    }

    /**
     * 检查属性是否已经设置
     *
     * @param channel Channel
     * @param key     AttributeKey
     * @param <T>     属性值的类型
     * @return true 如果属性已经设置，false 否则
     */
    public static <T> boolean hasAttribute(Channel channel, AttributeKey<T> key) {
        return channel.hasAttr(key);
    }

    /**
     * 移除属性
     *
     * @param channel Channel
     * @param key     AttributeKey
     * @param <T>     属性值的类型
     */
    public static <T> void removeAttribute(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        attr.set(null); // 在 Netty 中直接 set(null) 会将属性移除
    }

    /**
     * 设置 manager 上下文信息
     *
     * @param channel 渠道
     * @param manager 上下文信息
     */
    public static void setManager(Channel channel, RpcServerManager manager) {
        Attribute<RpcServerManager> attr = channel.attr(AttributeKey.valueOf("RpcServerManager"));
        attr.set(manager);
    }

    /**
     * 获取 manager 上下文信息
     *
     * @param channel 渠道
     * @return {@link RpcServerManager }
     */
    public static RpcServerManager getManager(Channel channel) {
        Attribute<RpcServerManager> attr = channel.attr(AttributeKey.valueOf("RpcServerManager"));
        return attr.get();
    }

}
