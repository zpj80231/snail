package com.snail.framework.redis.delay.domain;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 延迟消息实体
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Data
public class DelayMessage<T> implements Serializable {

    private static final long serialVersionUID = -9144144457629868649L;

    /**
     * 队列号
     */
    @NonNull
    private String queues;

    /**
     * 延迟时间
     */
    long delay;

    /**
     * 时间单位
     */
    TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 头
     */
    private Map<String, Object> headers;

    /**
     * 消息 ID
     */
    private String messageId;

    /**
     * 消息体
     */
    private T messageBody;

}
