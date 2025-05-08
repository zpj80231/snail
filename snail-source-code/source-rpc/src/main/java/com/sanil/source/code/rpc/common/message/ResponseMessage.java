package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;
import lombok.Data;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Data
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
