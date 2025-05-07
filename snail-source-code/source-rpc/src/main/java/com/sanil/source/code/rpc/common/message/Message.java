package com.sanil.source.code.rpc.common.message;

import com.sanil.source.code.rpc.common.enums.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础消息类
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Data
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 2543388113851841657L;

    /**
     * 序列 ID，每个消息是唯一的
     */
    private long sequenceId;
    /**
     * 消息类型，表示不同消息的类型，比如：登录、退出、各种请求、各种响应、心跳、异常等
     */
    private MessageTypeEnum messageType;

    /**
     * 获取消息类型，留待子类实现
     */
    public abstract MessageTypeEnum getMessageType();

}
