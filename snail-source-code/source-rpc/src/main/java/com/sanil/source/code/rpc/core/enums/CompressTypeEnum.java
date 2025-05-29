package com.sanil.source.code.rpc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangpj
 * @date 2025/5/29
 */
@Getter
@AllArgsConstructor
public enum CompressTypeEnum {

    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
