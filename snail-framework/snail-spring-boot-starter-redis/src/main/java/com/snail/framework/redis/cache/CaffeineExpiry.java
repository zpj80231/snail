package com.snail.framework.redis.cache;

import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author zhangpengjun
 * @date 2024/6/5
 */
public class CaffeineExpiry implements Expiry<String, Object> {

    @Override
    public long expireAfterCreate(@NonNull String key, @NonNull Object value, long currentTime) {
        return 0;
    }

    @Override
    public long expireAfterUpdate(@NonNull String key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }

    @Override
    public long expireAfterRead(@NonNull String key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }
}
