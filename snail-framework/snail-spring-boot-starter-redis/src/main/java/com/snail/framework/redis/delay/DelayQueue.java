package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;

import java.util.concurrent.TimeUnit;

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
     * @param value     值
     * @param delay     延迟时间
     * @param unit      延迟时间单位
     * @param queueName 队列名称
     */
    <T> void offer(DelayMessage<T> value, long delay, TimeUnit unit, String queueName);

    /**
     * 获取元素
     *
     * @param queueName 队列名称
     * @return {@link T }
     * @throws InterruptedException 中断异常
     */
    <T> DelayMessage<T> take(String queueName) throws InterruptedException;

}
