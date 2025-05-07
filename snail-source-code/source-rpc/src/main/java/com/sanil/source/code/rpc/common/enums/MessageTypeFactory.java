package com.sanil.source.code.rpc.common.enums;

import com.sanil.source.code.rpc.common.message.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
public class MessageTypeFactory {

    private static final MessageTypeFactory INSTANCE = new MessageTypeFactory();
    private static final Map<Integer, Class<? extends Message>> messageClassMap = new HashMap<>();

    private MessageTypeFactory() {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            int type = messageTypeEnum.getType();
            messageClassMap.put(type, messageTypeEnum.getClazz());
        }
    }

    public static MessageTypeFactory getInstance() {
        return INSTANCE;
    }

    public Class<? extends Message> getMessageType(int messageType) {
        return messageClassMap.get(messageType);
    }

}
