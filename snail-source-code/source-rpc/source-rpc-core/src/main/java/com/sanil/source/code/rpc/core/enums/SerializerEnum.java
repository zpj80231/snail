package com.sanil.source.code.rpc.core.enums;

import cn.hutool.core.map.BiMap;
import com.sanil.source.code.rpc.core.serialize.JavaSerializer;
import com.sanil.source.code.rpc.core.serialize.JsonSerializer;
import com.sanil.source.code.rpc.core.serialize.Serializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * serializer enum
 * <p>
 * 可扩展，SerializerEnum#register
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum SerializerEnum {

    JAVA((byte)0x01, "java", JavaSerializer.class),
    JSON((byte)0x02,"json", JsonSerializer.class),
    ;

    private final byte type;
    private final String name;
    private final Class<? extends Serializer> clazz;

    private static final BiMap<Byte, String> TYPE_TO_NAME = new BiMap<>(new HashMap<>());
    private static final Map<Byte, Serializer> TYPE_TO_CLASS = new HashMap<>();
    static {
        init();
    }

    /**
     * 获取序列化器
     *
     * @param serializerType 序列化器类型
     * @return {@link Serializer }
     */
    public static Serializer getSerializer(byte serializerType) {
        Serializer serializer = TYPE_TO_CLASS.get(serializerType);
        if (serializer == null) {
            return TYPE_TO_CLASS.get(JSON.getType());
        }
        return serializer;
    }

    /**
     * 获取类型
     *
     * @param name 名字
     * @return byte
     */
    public static byte getType(String name) {
        Byte type = TYPE_TO_NAME.getKey(name);
        if (type != null) {
            return type;
        }
        log.warn("serializer name {} not found, use default serializer", name);
        return JSON.getType();
    }

    /**
     * 获取名称
     *
     * @param type 类型
     * @return {@link String }
     */
    public static String getName(byte type) {
        String name = TYPE_TO_NAME.get(type);
        if (name != null) {
            return name;
        }
        log.warn("serializer type {} not found, use default serializer", type);
        return JSON.getName();
    }

    /**
     * 注册
     *
     * @param type       类型
     * @param name       名字
     * @param serializer 序列化器
     */
    public static synchronized void register(byte type, String name, Serializer serializer) {
        TYPE_TO_NAME.put(type, name);
        TYPE_TO_CLASS.put(type, serializer);
    }

    /**
     * 初始化加载默认序列化器
     */
    public static void init() {
        for (SerializerEnum serializerEnum : values()) {
            String name = serializerEnum.getName();
            Byte type = serializerEnum.getType();
            Serializer instance = null;
            try {
                instance = serializerEnum.getClazz().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("init serializer error of {}", name, e);
                continue;
            }
            TYPE_TO_NAME.put(type, name);
            TYPE_TO_CLASS.put(type, instance);
        }
    }

}
