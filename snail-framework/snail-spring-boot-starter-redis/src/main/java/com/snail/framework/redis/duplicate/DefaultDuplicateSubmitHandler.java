package com.snail.framework.redis.duplicate;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Slf4j
public class DefaultDuplicateSubmitHandler implements DuplicateSubmitHandler {

    @Override
    public Object handle(String cacheKey, DuplicateSubmit annotation, Method method, Object[] args) {
        // thew Exception or return value;
        log.warn("DuplicateSubmit, cacheKey: {}, msg: {}", cacheKey, annotation.message());
        return null;
    }

}
