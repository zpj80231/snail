package com.snail.framework.redis.cache;

import com.snail.framework.redis.common.RedisConstant;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 双缓存，local + redis，支持各自开关
 *
 * @author zhangpengjun
 * @date 2024/5/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {

    /**
     * 缓存名称
     *
     * @return {@link String }
     */
    String name() default RedisConstant.DOUBLE_CACHE;

    /**
     * spl 待解析的 key。默认为：类全名#方法名#参数值md5值
     *
     * @return {@link String }
     */
    String key() default "";

    /**
     * Redis 过期时间，单位，秒。默认 5 分钟。
     *
     * @return long
     */
    long expireOfLocal() default 5 * 60L;

    /**
     * Redis 过期时间，单位，秒。默认 30 分钟。
     *
     * @return long
     */
    long expireOfRedis() default 30 * 60L;

    /**
     *  过期时间单位，默认：秒。
     *  <b/>
     *  注意：expireOfLocal 和 expireOfRedis 都使用该单位。
     *
     * @return {@link TimeUnit }
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 缓存类型
     *
     * @return {@link DoubleCacheType }
     */
    DoubleCacheType cacheType() default DoubleCacheType.GET;

    /**
     * 本地缓存，默认开启
     *
     * @return boolean
     */
    boolean localCached() default true;

    /**
     * Redis 缓存，默认开启
     *
     * @return boolean
     */
    boolean redisCached() default true;

}
