package com.sanil.source.code.rpc.core.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class JsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz, Feature.SupportClassForName);
    }

}
