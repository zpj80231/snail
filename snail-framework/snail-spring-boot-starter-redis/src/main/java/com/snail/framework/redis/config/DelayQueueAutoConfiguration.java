package com.snail.framework.redis.config;

import com.snail.framework.redis.delay.*;
import com.snail.framework.redis.delay.interceptor.*;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 延迟队列自动配置启动类
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Configuration
@AutoConfigureAfter({ RedisAutoConfiguration.class, RedissonAutoConfiguration.class })
@ConditionalOnBean(RedissonClient.class)
public class DelayQueueAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DelayQueue.class)
    public RedisDelayQueue delayQueue(RedissonClient redissonClient, DelayMessageInterceptor delayMessageInterceptor) {
        return new RedisDelayQueue(redissonClient, delayMessageInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageProducerInterceptor.class)
    public DelayMessageProducerInterceptor delayMessageProducerInterceptor(@Lazy DelayQueue delayQueue) {
        return new DefaultDelayMessageProducerInterceptor(delayQueue);
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageConsumerInterceptor.class)
    public DelayMessageConsumerInterceptor delayMessageConsumerInterceptor() {
        return new DefaultDelayMessageConsumerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageInterceptor.class)
    public DelayMessageInterceptor delayMessageProducer(DelayMessageProducerInterceptor producerInterceptor,
                                                        DelayMessageConsumerInterceptor consumerInterceptor) {
        return new DefaultDelayMessageInterceptor(producerInterceptor, consumerInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean(DelayMessageDeadLetterConsumer.class)
    public DelayMessageDeadLetterConsumer delayMessageConsumer() {
        return new DefaultDelayMessageDeadLetterConsumer();
    }

    @Bean
    @ConditionalOnBean({DelayMessageDeadLetterConsumer.class, DelayQueue.class})
    public DelayMessageConsumerContainerLauncher delayMessageConsumerContainerLauncher(DelayQueue delayQueue) {
        return new DelayMessageConsumerContainerLauncher(delayQueue, delayMessageConsumer());
    }

}
