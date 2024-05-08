package com.snail.framework.redis.delay;

import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class RedisDelayMessageProducer implements DelayMessageProducer{

    private final DelayQueue delayQueue;

    public RedisDelayMessageProducer(DelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public <T> void sendMessage(DelayMessage<T> message, long delay, TimeUnit timeUnit, String queueName) {
        if (!delayQueue.containsConsumerContainer(queueName)) {
            log.error("No consumer container found for queue: {}, message: {}", queueName, JSON.toJSONString(message));
            return;
        }
        delayQueue.offer(message, delay, timeUnit, queueName);
    }

}
