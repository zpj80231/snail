package com.snail.framework.redis.config;

import com.snail.framework.redis.delay.*;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Configuration
@ConditionalOnBean(RedissonClient.class)
public class DelayQueueAutoConfiguration {

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    @ConditionalOnMissingBean(DelayQueue.class)
    public DelayQueue delayQueue() {
        return new RedisDelayQueue(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageProducer.class)
    public DelayMessageProducer delayMessageProducer() {
        return new RedisDelayMessageProducer(delayQueue());
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageDeadLetterConsumer.class)
    public DelayMessageDeadLetterConsumer delayMessageConsumer() {
        return new DefaultDelayMessageDeadLetterConsumer();
    }

    @Bean
    @ConditionalOnBean({DelayMessageDeadLetterConsumer.class, DelayQueue.class})
    public DelayMessageConsumerContainerLauncher delayMessageConsumerContainerLauncher() {
        return new DelayMessageConsumerContainerLauncher(delayQueue(), delayMessageConsumer());
    }

}
