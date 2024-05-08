package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayMessageProducer {

    <T> void sendMessage(DelayMessage<T> message, long delay, TimeUnit timeUnit, String queueName);

}
