package com.snail.framework.redis.cache;

import com.snail.framework.redis.common.RedisConstant;

import java.lang.annotation.*;

/**
 * 双缓存，local + redis，支持各自开关
 * <p>
 * 自定义本地缓存，注入一个 Bean {@link com.github.benmanes.caffeine.cache.Cache}，
 * 否则使用默认本地缓存（过期时间5分钟）
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
     * Redis 过期时间，单位，秒。
     *
     * @return long
     */
    long expireOfRedis() default 30 * 60L;

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
