package com.sanil.source.rpc.core.message;

import com.sanil.source.rpc.core.enums.MessageTypeEnum;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class PongMessage extends Message {

    private static final long serialVersionUID = 6791047642184473090L;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.PONG;
    }

}
