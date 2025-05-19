package com.sanil.source.code.rpc.core.message;

import com.sanil.source.code.rpc.core.enums.MessageTypeEnum;
import lombok.Data;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Data
public class RequestMessage extends Message {

    private static final long serialVersionUID = 5860473646720164597L;

    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数值
     */
    private Object[] parameterValues;
    /**
     * 返回类型
     */
    private Class<?> returnType;

    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.REQUEST;
    }
}
