package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class ResponseMessage extends Message {
    private static final long serialVersionUID = 8134854090045617859L;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.RESPONSE;
    }
}
