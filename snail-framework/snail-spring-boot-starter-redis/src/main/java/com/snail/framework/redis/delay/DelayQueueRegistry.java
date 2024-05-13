package com.snail.framework.redis.delay;

import java.util.Map;

/**
 * 延迟队列消费容器注册表
 *
 * @author zhangpengjun
 * @date 2024/5/8
 */
public interface DelayQueueRegistry {

    /**
     * 获取所有消费器
     *
     * @return {@link Map }<{@link String }, {@link DelayMessageConsumerContainer }>
     */
    Map<String, DelayMessageConsumerContainer> getConsumerContainers();

    /**
     * 获取一个具体的消费器
     *
     * @param queueName 队列名称
     * @return {@link DelayMessageConsumerContainer }
     */
    DelayMessageConsumerContainer getConsumerContainer(String queueName);

    /**
     * 是否包含一个具体的消费器
     *
     * @param queueName 队列名称
     * @return boolean
     */
    boolean containsConsumerContainer(String queueName);

}
