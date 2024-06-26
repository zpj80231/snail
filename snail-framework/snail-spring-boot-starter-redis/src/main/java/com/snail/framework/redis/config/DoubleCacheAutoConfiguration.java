package com.snail.framework.redis.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.snail.framework.redis.cache.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Configuration
@AutoConfigureAfter({ RedisAutoConfiguration.class })
@ConditionalOnBean(RedisTemplate.class)
@EnableAspectJAutoProxy
public class DoubleCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Cache.class)
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .initialCapacity(128) // 初始大小
                .maximumSize(5000) // 最大数量
                // .expireAfterWrite(5, TimeUnit.MINUTES) // 统一过期时间
                .expireAfter(new CaffeineExpiry()) // 自定义过期时间
                .build();
    }

    @Bean
    @ConditionalOnBean(Cache.class)
    public LocalCache localCache(Cache<String, Object> caffeineCache) {
        return new LocalCache(caffeineCache);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisCache redisCache(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisCache(redisTemplate);
    }

    @Bean
    @ConditionalOnBean({ LocalCache.class, RedisCache.class })
    public CacheManager cacheManager(LocalCache localCache, RedisCache redisCache) {
        return new CacheManager(localCache, redisCache);
    }

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public DoubleCacheAspect doubleCacheAspect(CacheManager cacheManager) {
        return new DoubleCacheAspect(cacheManager);
    }

}
