package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;
import com.snail.framework.redis.delay.interceptor.DelayMessageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redis 延迟队列
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class RedisDelayQueue extends DefaultDelayQueueRegistry implements DelayQueue {

    private final RedissonClient redissonClient;
    private final DelayMessageInterceptor delayMessageInterceptor;

    public RedisDelayQueue(RedissonClient redissonClient, DelayMessageInterceptor delayMessageInterceptor) {
        this.redissonClient = redissonClient;
        this.delayMessageInterceptor = delayMessageInterceptor;
    }

    @Override
    public <T> void offer(DelayMessage<T> message) {
        if (!delayMessageInterceptor.producerIntercept(message)) {
            return;
        }
        String queues = message.getQueues();
        long delay = message.getDelay();
        TimeUnit unit = message.getTimeUnit();
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queues);
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(message, delay, unit);
    }

    @Override
    public <T> DelayMessage<T> take(String queueName) throws InterruptedException {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueName);
        DelayMessage<T> message = (DelayMessage<T>) blockingDeque.take();
        if (!delayMessageInterceptor.consumerIntercept(message)) {
            return null;
        }
        return message;
    }

}
