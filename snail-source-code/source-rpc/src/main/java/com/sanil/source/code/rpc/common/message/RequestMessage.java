package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class RequestMessage extends Message {
    private static final long serialVersionUID = 5860473646720164597L;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.REQUEST;
    }
}
