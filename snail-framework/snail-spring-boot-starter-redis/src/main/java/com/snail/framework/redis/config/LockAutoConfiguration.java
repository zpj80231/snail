package com.snail.framework.redis.config;

import com.snail.framework.redis.lock.RedisLockAspect;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Configuration
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnBean(RedissonClient.class)
@EnableAspectJAutoProxy
public class LockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }

}
