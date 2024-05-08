package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
@Component
public class RedisDelayQueue extends DefaultDelayQueueRegistry implements DelayQueue {

    private final RedissonClient redissonClient;

    public RedisDelayQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> void offer(DelayMessage<T> message, long delay, TimeUnit unit, String queueName) {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueName);
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(message, delay, unit);
        log.info("Added message: {} to queue: {} with delay: {} {}", message, queueName, delay, unit.toString());
    }

    @Override
    public <T> DelayMessage<T> take(String queueName) throws InterruptedException {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueName);
        DelayMessage<T> value = (DelayMessage<T>) blockingDeque.take();
        return value;
    }

}
