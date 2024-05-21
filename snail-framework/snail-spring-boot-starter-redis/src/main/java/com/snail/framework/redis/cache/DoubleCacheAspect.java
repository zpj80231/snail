package com.snail.framework.redis.cache;

import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.common.RedisConstant;
import com.snail.framework.redis.util.ElParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Slf4j
@Aspect
@Order
public class DoubleCacheAspect {

    private final CacheManager cacheManager;

    public DoubleCacheAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Around("@annotation(doubleCache)")
    public Object doAround(ProceedingJoinPoint point, DoubleCache doubleCache) throws Throwable {
        String cacheName = doubleCache.name();
        String key = doubleCache.key();
        boolean localCached = doubleCache.localCached();
        boolean redisCached = doubleCache.redisCached();
        long expireOfRedis = doubleCache.expireOfRedis();
        DoubleCacheType cacheType = doubleCache.cacheType();
        if (!localCached && !redisCached) {
            return point.proceed();
        }

        String cacheKey = ElParser.getParseKey(cacheName, RedisConstant.DOUBLE_CACHE, key, point);
        if (cacheType == DoubleCacheType.PUT) {
            Object object = point.proceed();
            cacheManager.set(cacheKey, object, doubleCache);
            return object;
        } else if (cacheType == DoubleCacheType.DELETE) {
            cacheManager.delete(cacheKey, doubleCache);
            return point.proceed();
        }

        Object cacheValue = cacheManager.get(cacheKey, doubleCache);
        if (Objects.nonNull(cacheValue)) {
            return cacheValue;
        }
        Object proceed = point.proceed();
        if (log.isDebugEnabled()) {
            log.debug("DoubleCache GET from proceed, cacheKey: {}, proceed: {}", cacheKey, proceed);
        }
        if (Objects.nonNull(proceed)) {
            if (log.isDebugEnabled()) {
                log.debug("DoubleCache GET from proceed and SET, cacheKey: {}, expireOfRedis: {}, value: {}",
                        cacheKey, expireOfRedis, JSON.toJSONString(proceed));
            }
            cacheManager.set(cacheKey, proceed, doubleCache);
        }
        return proceed;
    }

}

