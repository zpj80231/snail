package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class ResponseMessage extends Message {

    private static final long serialVersionUID = 4043827980668524857L;

    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Throwable exceptionValue;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.RESPONSE;
    }
}
