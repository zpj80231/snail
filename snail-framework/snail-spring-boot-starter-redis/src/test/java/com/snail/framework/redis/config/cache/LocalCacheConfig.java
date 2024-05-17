package com.snail.framework.redis.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/17
 */
@Configuration
public class LocalCacheConfig {

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .initialCapacity(128) // 初始大小
                .maximumSize(5000) // 最大数量
                .expireAfterWrite(2, TimeUnit.SECONDS) // 过期时间
                .build();
    }

}
