package com.sanil.source.code.rpc.common.enums;

import com.sanil.source.code.rpc.common.message.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    PING(0, "PING", PingMessage.class),
    PONG(1, "PONG", PongMessage.class),
    REQUEST(1001, "请求", RequestMessage.class),
    RESPONSE(2001, "响应", ResponseMessage.class),
    ;

    /**
     * 类型
     */
    private final int type;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 类
     */
    private final Class<? extends Message> clazz;

}
