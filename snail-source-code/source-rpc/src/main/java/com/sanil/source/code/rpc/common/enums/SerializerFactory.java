package com.sanil.source.code.rpc.common.enums;

import cn.hutool.core.collection.CollUtil;
import com.sanil.source.code.rpc.common.codec.JsonSerializer;
import com.sanil.source.code.rpc.common.codec.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
public class SerializerFactory {

    private static final Serializer DEFAULT_SERIALIZER = new JsonSerializer();
    private static final Map<Integer, Serializer> SERIALIZER_MAP = new HashMap<>();

    public static Serializer getSerializer(int serializerType) {
        if (CollUtil.isEmpty(SERIALIZER_MAP)) {
            init();
        }
        Serializer serializer = SERIALIZER_MAP.get(serializerType);
        if (serializer == null) {
            return DEFAULT_SERIALIZER;
        }
        return serializer;
    }

    public static int getSerializerType(String serializerName) {
        for (SerializerEnum serializerEnum : SerializerEnum.values()) {
            if (serializerEnum.getName().equals(serializerName)) {
                return serializerEnum.getType();
            }
        }
        log.warn("serializerName {} not found, use default serializer", serializerName);
        return SerializerEnum.JSON.getType();
    }

    public static void init() {
        for (SerializerEnum serializerEnum : SerializerEnum.values()) {
            String name = serializerEnum.getName();
            int type = serializerEnum.getType();
            Serializer instance = null;
            try {
                instance = serializerEnum.getClazz().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("init serializer error of {}", name, e);
                continue;
            }
            SERIALIZER_MAP.put(type, instance);
        }
    }
}
