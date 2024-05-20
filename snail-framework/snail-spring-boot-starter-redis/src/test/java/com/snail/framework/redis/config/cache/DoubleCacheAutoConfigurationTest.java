package com.snail.framework.redis.config.cache;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.snail.framework.redis.config.DoubleCacheAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author zhangpj
 * @date 2024/05/17
 */
@Slf4j
@Import({RedisAutoConfiguration.class, LocalCacheConfig.class, Dog.class})
@SpringBootTest(classes = {DoubleCacheAutoConfiguration.class})
@EnableAspectJAutoProxy
class DoubleCacheAutoConfigurationTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired(required = false)
    private LocalCacheConfig localCacheConfig;
    @Autowired
    private Dog dog;

    @Test
    void testRedisTemplateExists() {
        assertNotNull(redisTemplate, "redisTemplate bean should not be null");
        assertNotNull(localCacheConfig, "localCacheConfig bean should not be null");
    }

    /**
     * 测试二级缓存
     * 2024-05-17 18:43:13.819  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 18:43:13.835 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:13.835 DEBUG 69763 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:14.061  INFO 69763 --- [           main] c.s.framework.redis.config.cache.Dog     : queryDatas from db ...
     * 2024-05-17 18:43:14.061 DEBUG 69763 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, proceed: {a=a}
     * 2024-05-17 18:43:14.114 DEBUG 69763 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, expireOfRedis: 5, value: {"a":"a"}
     * 2024-05-17 18:43:14.115 DEBUG 69763 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, value: {"a":"a"}, expire: 5, timeUnit: SECONDS
     * 2024-05-17 18:43:14.126 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, value: {a=a}
     * 2024-05-17 18:43:14.126  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第一次，模拟从数据库查询数据：{a=a}
     * 2024-05-17 18:43:14.126  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 18:43:15.133 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:15.136  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第二次，从本地缓存获取数据：{a=a}
     * 2024-05-17 18:43:15.137  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 18:43:17.137 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:17.138 DEBUG 69763 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:17.150  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第三次，从Redis缓存获取数据：{a=a}
     * 2024-05-17 18:43:17.150  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 18:43:22.156 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:22.157 DEBUG 69763 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8
     * 2024-05-17 18:43:22.167  INFO 69763 --- [           main] c.s.framework.redis.config.cache.Dog     : queryDatas from db ...
     * 2024-05-17 18:43:22.168 DEBUG 69763 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, proceed: {a=a}
     * 2024-05-17 18:43:22.168 DEBUG 69763 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, expireOfRedis: 5, value: {"a":"a"}
     * 2024-05-17 18:43:22.168 DEBUG 69763 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, value: {"a":"a"}, expire: 5, timeUnit: SECONDS
     * 2024-05-17 18:43:22.179 DEBUG 69763 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatas#a956af09162870af6ce4ebe0f12ccaf8, value: {a=a}
     * 2024-05-17 18:43:22.179  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第四次，所有缓存时间到期，再次从数据库查询数据：{a=a}
     * 2024-05-17 18:43:22.179  INFO 69763 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     *
     * @throws NoSuchMethodException 没有这样方法例外
     */
    @Test
    void testDoubleCacheAspect() throws NoSuchMethodException {
        dog.setDatas(MapUtil.of("a", "a"));
        log.info("-----------------------");
        log.info("第一次，模拟从数据库查询数据：{}", dog.queryDatas());
        log.info("-----------------------");
        ThreadUtil.sleep(1000);
        log.info("第二次，从本地缓存获取数据：{}", dog.queryDatas());
        log.info("-----------------------");
        ThreadUtil.sleep(2000);
        log.info("第三次，从Redis缓存获取数据：{}", dog.queryDatas());
        log.info("-----------------------");
        ThreadUtil.sleep(5000);
        log.info("第四次，所有缓存时间到期，再次从数据库查询数据：{}", dog.queryDatas());
        log.info("-----------------------");
    }

    /**
     * 使用 el表达式的 args，测试二级缓存
     * 2024-05-17 19:18:42.871  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:18:42.888 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:42.888 DEBUG 70196 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:43.117  INFO 70196 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithKey:[a] from db
     * 2024-05-17 19:18:43.117 DEBUG 70196 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:18:43.158 DEBUG 70196 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:18:43.158 DEBUG 70196 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 5, timeUnit: SECONDS
     * 2024-05-17 19:18:43.169 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a
     * 2024-05-17 19:18:43.170  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第一次，模拟从数据库查询数据：a
     * 2024-05-17 19:18:43.170  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:18:44.176 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:44.180  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第二次，从本地缓存获取数据：a
     * 2024-05-17 19:18:44.181  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:18:46.187 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:46.188 DEBUG 70196 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:46.203  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第三次，从Redis缓存获取数据：a
     * 2024-05-17 19:18:46.203  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:18:51.205 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:51.205 DEBUG 70196 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:18:51.216  INFO 70196 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithKey:[a] from db
     * 2024-05-17 19:18:51.217 DEBUG 70196 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:18:51.217 DEBUG 70196 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:18:51.217 DEBUG 70196 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 5, timeUnit: SECONDS
     * 2024-05-17 19:18:51.227 DEBUG 70196 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a
     * 2024-05-17 19:18:51.227  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第四次，所有缓存时间到期，再次从数据库查询数据：a
     * 2024-05-17 19:18:51.227  INFO 70196 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     *
     * @throws NoSuchMethodException
     */
    @Test
    void testDoubleCacheAspectWithArgs() throws NoSuchMethodException {
        dog.setDatas(MapUtil.of("a", "a"));
        String key = "a";
        log.info("-----------------------");
        log.info("第一次，模拟从数据库查询数据：{}", dog.queryValueWithElKey(key));
        log.info("-----------------------");
        ThreadUtil.sleep(1000);
        log.info("第二次，从本地缓存获取数据：{}", dog.queryValueWithElKey(key));
        log.info("-----------------------");
        ThreadUtil.sleep(2000);
        log.info("第三次，从Redis缓存获取数据：{}", dog.queryValueWithElKey(key));
        log.info("-----------------------");
        ThreadUtil.sleep(5000);
        log.info("第四次，所有缓存时间到期，再次从数据库查询数据：{}", dog.queryValueWithElKey(key));
        log.info("-----------------------");
    }

    /**
     * 仅测试本地缓存
     * 2024-05-17 19:28:40.345  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:28:40.360 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:28:40.360  INFO 70322 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithElKeyOnlyLocalCache:[a] from db
     * 2024-05-17 19:28:40.360 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:28:40.396 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:28:40.396 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a
     * 2024-05-17 19:28:40.396  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第一次，模拟从数据库查询数据：a
     * 2024-05-17 19:28:40.397  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:28:41.401 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:28:41.405  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第二次，从本地缓存获取数据：a
     * 2024-05-17 19:28:41.406  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:28:43.408 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:28:43.409  INFO 70322 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithElKeyOnlyLocalCache:[a] from db
     * 2024-05-17 19:28:43.409 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:28:43.409 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:28:43.409 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a
     * 2024-05-17 19:28:43.412  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第三次，从本地缓存获取不到数据，再次从数据库查询数据：a
     * 2024-05-17 19:28:43.413  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:28:48.416 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:28:48.417  INFO 70322 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithElKeyOnlyLocalCache:[a] from db
     * 2024-05-17 19:28:48.418 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:28:48.418 DEBUG 70322 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:28:48.418 DEBUG 70322 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a
     * 2024-05-17 19:28:48.418  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第四次，所有缓存时间到期，再次从数据库查询数据：a
     * 2024-05-17 19:28:48.419  INFO 70322 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     */
    @Test
    void testOnlyLocalCache() {
        dog.setDatas(MapUtil.of("a", "a"));
        String key = "a";
        log.info("-----------------------");
        log.info("第一次，模拟从数据库查询数据：{}", dog.queryValueWithElKeyOnlyLocalCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(1000);
        log.info("第二次，从本地缓存获取数据：{}", dog.queryValueWithElKeyOnlyLocalCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(2000);
        log.info("第三次，从本地缓存获取不到数据，再次从数据库查询数据：{}", dog.queryValueWithElKeyOnlyLocalCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(5000);
        log.info("第四次，所有缓存时间到期，再次从数据库查询数据：{}", dog.queryValueWithElKeyOnlyLocalCache(key));
        log.info("-----------------------");
    }

    /**
     * 仅测试 Redis 缓存
     * 2024-05-17 19:31:29.825  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:31:29.841 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:31:30.050  INFO 70359 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithElKeyOnlyRedisCache:[a] from db
     * 2024-05-17 19:31:30.050 DEBUG 70359 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:31:30.086 DEBUG 70359 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:31:30.087 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 5, timeUnit: SECONDS
     * 2024-05-17 19:31:30.097  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第一次，模拟从数据库查询数据：a
     * 2024-05-17 19:31:30.097  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:31:31.104 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:31:31.116  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第二次，从Redis缓存获取数据：a
     * 2024-05-17 19:31:31.116  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:31:33.122 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:31:33.134  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第三次，也是从Redis缓存获取数据：a
     * 2024-05-17 19:31:33.135  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     * 2024-05-17 19:31:38.142 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-05-17 19:31:38.359  INFO 70359 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithElKeyOnlyRedisCache:[a] from db
     * 2024-05-17 19:31:38.359 DEBUG 70359 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-05-17 19:31:38.359 DEBUG 70359 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 5, value: "a"
     * 2024-05-17 19:31:38.360 DEBUG 70359 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 5, timeUnit: SECONDS
     * 2024-05-17 19:31:38.369  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : 第四次，所有缓存时间到期，再次从数据库查询数据：a
     * 2024-05-17 19:31:38.369  INFO 70359 --- [           main] c.s.f.r.c.c.DoubleCacheAutoConfigTest    : -----------------------
     */
    @Test
    void testOnlyRedisCache() {
        dog.setDatas(MapUtil.of("a", "a"));
        String key = "a";
        log.info("-----------------------");
        log.info("第一次，模拟从数据库查询数据：{}", dog.queryValueWithElKeyOnlyRedisCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(1000);
        log.info("第二次，从Redis缓存获取数据：{}", dog.queryValueWithElKeyOnlyRedisCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(2000);
        log.info("第三次，也是从Redis缓存获取数据：{}", dog.queryValueWithElKeyOnlyRedisCache(key));
        log.info("-----------------------");
        ThreadUtil.sleep(5000);
        log.info("第四次，所有缓存时间到期，再次从数据库查询数据：{}", dog.queryValueWithElKeyOnlyRedisCache(key));
        log.info("-----------------------");
    }

}
