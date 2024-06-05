package com.snail.framework.redis.config.cache;

import com.snail.framework.redis.cache.DoubleCache;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2024/5/17
 */
@Slf4j
public class Dog {

    private static final Map<String, String> DATAS = new HashMap<>();

    public void setDatas(Map<String, String> datas) {
        DATAS.putAll(datas);
    }

    @DoubleCache(expireOfRedis = 5)
    public Map<String, String> queryDatas() {
        log.info("queryDatas from db ...");
        return DATAS;
    }

    @DoubleCache(expireOfRedis = 5, key = "#key")
    public String queryValueWithElKey(String key) {
        log.info("queryValueWithKey:[{}] from db", key);
        return DATAS.get(key);
    }

    @DoubleCache(expireOfRedis = 5, key = "#key", redisCached = false)
    public String queryValueWithElKeyOnlyLocalCache(String key) {
        log.info("queryValueWithElKeyOnlyLocalCache:[{}] from db", key);
        return DATAS.get(key);
    }

    @DoubleCache(expireOfRedis = 5, key = "#key", localCached = false)
    public String queryValueWithElKeyOnlyRedisCache(String key) {
        log.info("queryValueWithElKeyOnlyRedisCache:[{}] from db", key);
        return DATAS.get(key);
    }


    @DoubleCache(expireOfLocal = 2, expireOfRedis = 5)
    public Map<String, String> queryDatasCustomExpire() {
        log.info("queryDatasCustomExpire from db ...");
        return DATAS;
    }

    @DoubleCache(key = "#key", expireOfLocal = 6, expireOfRedis = 10)
    public String queryValueWithCustomExpire(String key) {
        log.info("queryValueWithCustomExpire:[{}] from db", key);
        return DATAS.get(key);
    }

}
