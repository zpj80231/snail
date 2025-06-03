package com.sanil.source.code.rpc.core.enums;

import cn.hutool.core.map.BiMap;
import com.sanil.source.code.rpc.core.compress.Compress;
import com.sanil.source.code.rpc.core.compress.GzipCompress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * CompressEnum enum
 * <p>
 * 可扩展，CompressEnum#register
 * @author zhangpj
 * @date 2025/5/29
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum CompressEnum {

    GZIP((byte) 0x01, "gzip", GzipCompress.class);

    private final byte type;
    private final String name;
    private final Class<? extends GzipCompress> clazz;

    private static final BiMap<Byte, String> TYPE_TO_NAME = new BiMap<>(new HashMap<>());
    private static final Map<Byte, Compress> TYPE_TO_CLASS = new HashMap<>();
    static {
        init();
    }

    /**
     * 获取压缩类
     *
     * @param type 压缩类类型
     * @return {@link Compress }
     */
    public static Compress getCompress(byte type) {
        Compress compress = TYPE_TO_CLASS.get(type);
        if (compress == null) {
            return TYPE_TO_CLASS.get(GZIP.getType());
        }
        return compress;
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
        log.warn("compress name {} not found, use default compress", name);
        return GZIP.getType();
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
        log.warn("compress type {} not found, use default compress", type);
        return GZIP.getName();
    }

    /**
     * 注册
     *
     * @param type       类型
     * @param name       名字
     * @param compress 压缩类
     */
    public static synchronized void register(byte type, String name, Compress compress) {
        TYPE_TO_NAME.put(type, name);
        TYPE_TO_CLASS.put(type, compress);
    }

    /**
     * 初始化加载默认压缩类
     */
    public static void init() {
        for (CompressEnum e : values()) {
            String name = e.getName();
            Byte type = e.getType();
            Compress instance = null;
            try {
                instance = e.getClazz().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                log.error("compress init error of {}", name, ex);
                continue;
            }
            TYPE_TO_NAME.put(type, name);
            TYPE_TO_CLASS.put(type, instance);
        }
    }

}
