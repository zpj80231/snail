package com.snail.framework.redis.delay.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Data
public class DelayMessage<T> implements Serializable {

    private static final long serialVersionUID = -9144144457629868649L;

    private String queues;

    private Map<String, Object> headers;

    private String messageId;

    private T messageBody;

}
