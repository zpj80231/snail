package com.sanil.source.code.rpc.core.serialize;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class JavaSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        if (object != null) {
            return ObjectUtil.serialize(object);
        }
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return ObjectUtil.deserialize(bytes);
    }

}
