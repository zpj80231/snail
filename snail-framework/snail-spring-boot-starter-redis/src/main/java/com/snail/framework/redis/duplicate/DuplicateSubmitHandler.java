package com.snail.framework.redis.duplicate;

import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
public interface DuplicateSubmitHandler {

    Object handle(String cacheKey, DuplicateSubmit annotation, Method method, Object[] args);

}
