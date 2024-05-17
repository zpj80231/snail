package com.snail.framework.redis.cache;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Slf4j
public class RedisCache {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisCache(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object get(String key) {
        if (log.isDebugEnabled()) {
            log.debug("RedisCache get key: {}", key);
        }
        return redisTemplate.opsForValue().get(key);
    }
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        if (log.isDebugEnabled()) {
            log.debug("RedisCache set key: {}, value: {}, expire: {}, timeUnit: {}",
                    key, JSON.toJSONString(value), expire, timeUnit);
        }
        redisTemplate.opsForValue().set(key, value, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    public void delete(String key) {
        if (log.isDebugEnabled()) {
            log.debug("RedisCache delete key: {}", key);
        }
        redisTemplate.delete(key);
    }

}

