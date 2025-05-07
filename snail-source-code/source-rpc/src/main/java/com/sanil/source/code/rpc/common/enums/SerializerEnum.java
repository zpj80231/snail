package com.sanil.source.code.rpc.common.enums;

import com.sanil.source.code.rpc.common.codec.JavaSerializer;
import com.sanil.source.code.rpc.common.codec.JsonSerializer;
import com.sanil.source.code.rpc.common.codec.Serializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Getter
@AllArgsConstructor
public enum SerializerEnum {

    JAVA(0, "java", JavaSerializer.class),
    JSON(1,"json", JsonSerializer.class),
    ;

    private final int type;
    private final String name;
    private final Class<? extends Serializer> clazz;

}
