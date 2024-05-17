package com.snail.framework.redis.cache;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.common.RedisConstant;
import com.snail.framework.redis.util.ElParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
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

        String cacheNamed = getCacheName(cacheName);
        String splKey = getSplKey(point, key);
        String cacheKey = cacheNamed + splKey;

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

    private static String getSplKey(ProceedingJoinPoint point, String key) {
        if (StrUtil.isBlank(key)) {
            StringBuilder sb = new StringBuilder("args");
            Arrays.stream(point.getArgs()).forEach(arg -> sb.append(JSON.toJSONString(arg)));
            String className = point.getSignature().getDeclaringTypeName();
            String methodName = point.getSignature().getName();
            key = className + "#" + methodName + "#" + SecureUtil.md5(sb.toString());
        } else {
            MethodSignature signature = (MethodSignature) point.getSignature();
            key = ElParser.parse(signature.getMethod(), point.getArgs(), key);
        }
        return key;
    }

    private static String getCacheName(String cacheName) {
        if (StrUtil.isBlank(cacheName)) {
            cacheName = RedisConstant.DOUBLE_CACHE;
        } else if (cacheName.lastIndexOf(":") == -1) {
            cacheName = cacheName + ":";
        }
        return cacheName;
    }

}

