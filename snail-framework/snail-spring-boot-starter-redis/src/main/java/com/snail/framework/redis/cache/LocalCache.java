package com.snail.framework.redis.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Slf4j
public class LocalCache {

    private final Cache<String, Object> cache;

    public LocalCache(Cache<String, Object> cache) {
        this.cache = cache;
    }

    public Object get(String key) {
        if (log.isDebugEnabled()) {
            log.debug("LocalCache get key: {}", key);
        }
        return cache.getIfPresent(key);
    }
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        if (log.isDebugEnabled()) {
            log.debug("LocalCache set key: {}, value: {}, expire: {}, timeUnit: {}", key, value, expire, timeUnit);
        }
        cache.policy().expireVariably().ifPresent(e -> e.put(key, value, expire, timeUnit));
    }

    public void delete(String key) {
        if (log.isDebugEnabled()) {
            log.debug("LocalCache delete key: {}", key);
        }
        cache.invalidate(key);
    }

}

