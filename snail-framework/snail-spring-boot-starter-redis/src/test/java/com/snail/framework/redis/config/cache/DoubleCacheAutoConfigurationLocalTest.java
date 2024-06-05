package com.snail.framework.redis.config.cache;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.snail.framework.redis.config.DoubleCacheAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 主要是为了测试 自定义本地缓存过期时间和自定义redis过期时间
 *
 * @author zhangpj
 * @date 2024/05/17
 */
@Slf4j
@Import({RedisAutoConfiguration.class, Dog.class})
@SpringBootTest(classes = {DoubleCacheAutoConfiguration.class})
class DoubleCacheAutoConfigurationLocalTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private Dog dog;

    @Test
    void testRedisTemplateExists() {
        assertNotNull(redisTemplate, "redisTemplate bean should not be null");
    }

    /**
     * 测试二级缓存，支持自定义本地缓存过期时间和自定义redis过期时间
     *
     * 2024-06-05 19:21:33.466  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     * 2024-06-05 19:21:33.480 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:33.480 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:33.700  INFO 99079 --- [           main] c.s.framework.redis.config.cache.Dog     : queryDatasCustomExpire from db ...
     * 2024-06-05 19:21:33.700 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, proceed: {a=a}
     * 2024-06-05 19:21:33.740 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, expireOfRedis: 5, value: {"a":"a"}
     * 2024-06-05 19:21:33.740 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, value: {"a":"a"}, expire: 5, timeUnit: SECONDS
     * 2024-06-05 19:21:33.753 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, value: {a=a}, expire: 2, timeUnit: SECONDS
     * 2024-06-05 19:21:33.754  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第一次，模拟从数据库查询数据 queryDatasCustomExpire：{a=a}
     * 2024-06-05 19:21:33.767 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:33.767 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:33.775  INFO 99079 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithCustomExpire:[a] from db
     * 2024-06-05 19:21:33.775 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-06-05 19:21:33.776 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 10, value: "a"
     * 2024-06-05 19:21:33.776 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 10, timeUnit: SECONDS
     * 2024-06-05 19:21:33.784 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a, expire: 6, timeUnit: SECONDS
     * 2024-06-05 19:21:33.784  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第一次，模拟从数据库查询数据 queryValueWithCustomExpire：a
     * 2024-06-05 19:21:33.784  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     * 2024-06-05 19:21:34.791 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:34.796  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第二次，从本地缓存获取数据 queryDatasCustomExpire：{a=a}
     * 2024-06-05 19:21:34.796 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:34.797  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第二次，从本地缓存获取数据 queryValueWithCustomExpire：a
     * 2024-06-05 19:21:34.797  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     * 2024-06-05 19:21:36.799 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:36.800 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:36.821  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第三次，从Redis缓存获取数据 queryDatasCustomExpire：{a=a}
     * 2024-06-05 19:21:36.821 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:36.822  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第三次，从本地缓存获取数据 queryValueWithCustomExpire：a
     * 2024-06-05 19:21:36.822  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     * 2024-06-05 19:21:41.828 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:41.828 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce
     * 2024-06-05 19:21:41.840  INFO 99079 --- [           main] c.s.framework.redis.config.cache.Dog     : queryDatasCustomExpire from db ...
     * 2024-06-05 19:21:41.840 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, proceed: {a=a}
     * 2024-06-05 19:21:41.840 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, expireOfRedis: 5, value: {"a":"a"}
     * 2024-06-05 19:21:41.841 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, value: {"a":"a"}, expire: 5, timeUnit: SECONDS
     * 2024-06-05 19:21:41.852 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:com.snail.framework.redis.config.cache.Dog#queryDatasCustomExpire#d751713988987e9331980363e24189ce, value: {a=a}, expire: 2, timeUnit: SECONDS
     * 2024-06-05 19:21:41.852  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第四次，queryDatasCustomExpire 所有缓存时间到期，再次从数据库查询数据：{a=a}
     * 2024-06-05 19:21:41.852 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:41.853 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:41.862  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第四次，从本地缓存获取数据 queryValueWithCustomExpire：a
     * 2024-06-05 19:21:41.862  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     * 2024-06-05 19:21:44.865 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:44.865 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache get key: DOUBLE_CACHE:a
     * 2024-06-05 19:21:44.876  INFO 99079 --- [           main] c.s.framework.redis.config.cache.Dog     : queryValueWithCustomExpire:[a] from db
     * 2024-06-05 19:21:44.877 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed, cacheKey: DOUBLE_CACHE:a, proceed: a
     * 2024-06-05 19:21:44.877 DEBUG 99079 --- [           main] c.s.f.redis.cache.DoubleCacheAspect      : DoubleCache GET from proceed and SET, cacheKey: DOUBLE_CACHE:a, expireOfRedis: 10, value: "a"
     * 2024-06-05 19:21:44.877 DEBUG 99079 --- [           main] c.s.framework.redis.cache.RedisCache     : RedisCache set key: DOUBLE_CACHE:a, value: "a", expire: 10, timeUnit: SECONDS
     * 2024-06-05 19:21:44.887 DEBUG 99079 --- [           main] c.s.framework.redis.cache.LocalCache     : LocalCache set key: DOUBLE_CACHE:a, value: a, expire: 6, timeUnit: SECONDS
     * 2024-06-05 19:21:44.887  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : 第五次，queryValueWithCustomExpire 所有缓存时间到期，再次从数据库查询数据：a
     * 2024-06-05 19:21:44.887  INFO 99079 --- [           main] .c.DoubleCacheAutoConfigurationLocalTest : -----------------------
     *
     * @throws NoSuchMethodException 没有这样方法例外
     */
    @Test
    void testDoubleCacheAspect() throws NoSuchMethodException {
        dog.setDatas(MapUtil.of("a", "a"));
        log.info("-----------------------");
        log.info("第一次，模拟从数据库查询数据 queryDatasCustomExpire：{}", dog.queryDatasCustomExpire());
        log.info("第一次，模拟从数据库查询数据 queryValueWithCustomExpire：{}", dog.queryValueWithCustomExpire("a"));
        log.info("-----------------------");
        ThreadUtil.sleep(1000);
        log.info("第二次，从本地缓存获取数据 queryDatasCustomExpire：{}", dog.queryDatasCustomExpire());
        log.info("第二次，从本地缓存获取数据 queryValueWithCustomExpire：{}", dog.queryValueWithCustomExpire("a"));
        log.info("-----------------------");
        ThreadUtil.sleep(2000);
        log.info("第三次，从Redis缓存获取数据 queryDatasCustomExpire：{}", dog.queryDatasCustomExpire());
        log.info("第三次，从本地缓存获取数据 queryValueWithCustomExpire：{}", dog.queryValueWithCustomExpire("a"));
        log.info("-----------------------");
        ThreadUtil.sleep(5000);
        log.info("第四次，queryDatasCustomExpire 所有缓存时间到期，再次从数据库查询数据：{}", dog.queryDatasCustomExpire());
        log.info("第四次，从本地缓存获取数据 queryValueWithCustomExpire：{}", dog.queryValueWithCustomExpire("a"));
        log.info("-----------------------");
        ThreadUtil.sleep(3000);
        log.info("第五次，queryValueWithCustomExpire 所有缓存时间到期，再次从数据库查询数据：{}", dog.queryValueWithCustomExpire("a"));
        log.info("-----------------------");
    }

}
