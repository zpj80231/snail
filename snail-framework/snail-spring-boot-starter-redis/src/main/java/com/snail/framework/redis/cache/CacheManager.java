package com.snail.framework.redis.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Slf4j
public class CacheManager {

    private final LocalCache localCache;
    private final RedisCache redisCache;

    public CacheManager(LocalCache localCache, RedisCache redisCache) {
        this.localCache = localCache;
        this.redisCache = redisCache;
    }

    public Object get(String key, DoubleCache doubleCache) {
        boolean localCached = doubleCache.localCached();
        boolean redisCached = doubleCache.redisCached();
        if (localCached) {
            Object localCacheObj = localCache.get(key);
            if (Objects.nonNull(localCacheObj)) {
                return localCacheObj;
            }
        }
        if (redisCached) {
            Object redisCacheObj = redisCache.get(key);
            if (Objects.nonNull(redisCacheObj)) {
                return redisCacheObj;
            }
        }
        return null;
    }

    public void set(String key, Object value, DoubleCache doubleCache) {
        boolean localCached = doubleCache.localCached();
        boolean redisCached = doubleCache.redisCached();
        long expireOfRedis = doubleCache.expireOfRedis();
        TimeUnit timeUnit = doubleCache.timeUnit();
        if (redisCached) {
            redisCache.set(key, value, expireOfRedis, timeUnit);
        }
        if (localCached) {
            localCache.set(key, value);
        }
    }

    public void delete(String key, DoubleCache doubleCache) {
        boolean localCached = doubleCache.localCached();
        boolean redisCached = doubleCache.redisCached();
        if (localCached) {
            localCache.delete(key);
        }
        if (redisCached) {
            redisCache.delete(key);
        }
    }

}

