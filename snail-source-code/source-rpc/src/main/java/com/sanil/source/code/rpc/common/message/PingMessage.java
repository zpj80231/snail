package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;

/**
 * ping 消息
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class PingMessage extends Message {

    private static final long serialVersionUID = 8723923272370774321L;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.PING;
    }

}
