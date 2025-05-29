package com.sanil.source.code.rpc.core.message;

import com.sanil.source.code.rpc.core.enums.MessageTypeEnum;
import com.sanil.source.code.rpc.core.util.RpcServiceUtil;
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
     * 接口分组
     */
    private String group;
    /**
     * 接口版本
     */
    private String version;
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

    public String getRpcServiceName() {
        return RpcServiceUtil.getProviderName(interfaceName, group, version);
    }

}
