package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;

/**
 * 延迟队列
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayQueue extends DelayQueueRegistry {

    /**
     * 添加元素
     *
     * @param message 消息
     */
    <T> void offer(DelayMessage<T> message);

    /**
     * 获取元素
     *
     * @param queueName 队列名称
     * @return {@link T }
     * @throws InterruptedException 中断异常
     */
    <T> DelayMessage<T> take(String queueName) throws InterruptedException;

}
