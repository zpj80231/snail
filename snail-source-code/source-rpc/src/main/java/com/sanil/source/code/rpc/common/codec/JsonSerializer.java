package com.sanil.source.code.rpc.common.codec;

import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class JsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        return JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), clazz);
    }

}
