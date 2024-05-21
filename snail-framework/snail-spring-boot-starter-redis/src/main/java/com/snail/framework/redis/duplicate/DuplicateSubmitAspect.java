package com.snail.framework.redis.duplicate;

import com.snail.framework.redis.common.RedisConstant;
import com.snail.framework.redis.util.ElParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Slf4j
@Aspect
public class DuplicateSubmitAspect {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final DuplicateSubmitHandler duplicateSubmitHandler;

    public DuplicateSubmitAspect(RedisTemplate<Object, Object> redisTemplate, DuplicateSubmitHandler duplicateSubmitHandler) {
        this.redisTemplate = redisTemplate;
        this.duplicateSubmitHandler = duplicateSubmitHandler;
    }

    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint point, DuplicateSubmit annotation) throws Throwable {
        String cacheKey = ElParser.getParseKey(null, RedisConstant.DUPLICATE_SUBMIT, annotation.key(), point);
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, annotation.expire(), annotation.timeUnit());
        if (log.isDebugEnabled()) {
            log.debug("Duplicate submit lock: {}, expire: {}, timeUnit: {}, locked: {}",
                    cacheKey, annotation.expire(), annotation.timeUnit(), locked);
        }
        if (locked == null || Boolean.FALSE.equals(locked)) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            return duplicateSubmitHandler.handle(cacheKey, annotation, signature.getMethod(), point.getArgs());
        }
        return point.proceed();
    }

}
