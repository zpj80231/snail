package com.snail.framework.redis.lock;

import com.snail.framework.redis.common.RedisConstant;
import com.snail.framework.redis.util.ElParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Slf4j
@Aspect
@Order
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    public RedisLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint point, Lock annotation) throws Throwable {
        long waitTime = annotation.waitTime();
        long leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();
        String keyPrefix = ElParser.getPrefixKey(null, RedisConstant.LOCK);
        String splKey = ElParser.parse(point, annotation.key(), false);
        String realKey = keyPrefix + splKey;
        RLock lock = redissonClient.getLock(realKey);
        try {
            if (waitTime < 0 && leaseTime < 0)  {
                if (log.isDebugEnabled()) {
                    log.debug("Get lock: {}", realKey);
                }
                lock.lock();
                return point.proceed();
            }
            if (waitTime < 0 && leaseTime > 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Get lock: {}, leaseTime: {}, timeUnit: {}", realKey, leaseTime, timeUnit);
                }
                lock.lock(leaseTime, timeUnit);
                return point.proceed();
            }
            if (waitTime >= 0 && lock.tryLock(waitTime, leaseTime, timeUnit)) {
                if (log.isDebugEnabled()) {
                    log.debug("Get tryLock: {}, waitTime: {}, leaseTime: {}, timeUnit: {}",
                            realKey, waitTime, leaseTime, timeUnit);
                }
                return point.proceed();
            }
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                if (log.isDebugEnabled()) {
                    log.debug("Release lock: {}", realKey);
                }
                lock.unlock();
            }
        }
        return point.proceed();
    }

}
