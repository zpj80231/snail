package com.snail.framework.redis.cache;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
public enum DoubleCacheType {

    /**
     * 查
     */
    GET,
    /**
     * 增（或强制更新）
     */
    PUT,
    /**
     * 删
     */
    DELETE

}
