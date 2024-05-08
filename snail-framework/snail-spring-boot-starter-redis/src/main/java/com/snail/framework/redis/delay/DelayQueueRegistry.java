package com.snail.framework.redis.delay;

import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2024/5/8
 */
public interface DelayQueueRegistry {

    Map<String, DelayMessageConsumerContainer> getConsumerContainers();

    DelayMessageConsumerContainer getConsumerContainer(String queueName);

    boolean containsConsumerContainer(String queueName);

}
