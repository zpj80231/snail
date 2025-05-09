package com.sanil.source.code.rpc.common.factory;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;
import com.sanil.source.code.rpc.common.message.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
public class MessageTypeFactory {

    private static final Map<Integer, Class<? extends Message>> messageClassMap = new HashMap<>();
    @Getter
    private static final MessageTypeFactory instance = new MessageTypeFactory();

    static {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            int type = messageTypeEnum.getType();
            messageClassMap.put(type, messageTypeEnum.getClazz());
        }
    }

    private MessageTypeFactory() {

    }

    public Class<? extends Message> getMessageType(int messageType) {
        return messageClassMap.get(messageType);
    }

}
