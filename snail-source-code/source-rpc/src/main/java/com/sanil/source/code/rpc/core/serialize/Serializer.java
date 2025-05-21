package com.sanil.source.code.rpc.core.serialize;

import com.sanil.source.code.rpc.core.extension.SPI;

/**
 * 序列化器
 * 用于扩展序列化、反序列化算法
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     *
     * @param object 对象
     * @return {@link byte[] }
     */
    <T> byte[] serialize(T object);

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param clazz clazz
     * @return {@link T }
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
