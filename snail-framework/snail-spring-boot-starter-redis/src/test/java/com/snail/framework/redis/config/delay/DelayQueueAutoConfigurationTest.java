package com.snail.framework.redis.config.delay;

import cn.hutool.core.thread.ThreadUtil;
import com.snail.framework.redis.config.DelayQueueAutoConfiguration;
import com.snail.framework.redis.delay.DelayQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author zhangpengjun
 * @date 2024/5/13
 */
@Slf4j
@SpringBootTest(classes = {DelayQueueAutoConfiguration.class})
@Import({RedissonAutoConfiguration.class, DelayQueueProducerConsumer.class, Cat.class})
class DelayQueueAutoConfigurationTest {

    @Autowired
    private DelayQueue delayQueue;
    @Autowired
    private DelayQueueProducerConsumer delayQueueProducerConsumer;

    @Test
    void testDelayQueueBeanExists() {
        assertNotNull(delayQueue, "DelayQueue bean should not be null");
        assertNotNull(delayQueueProducerConsumer, "DelayQueueTest bean should not be null");
    }

    @Test
    void testDelayQueue() {
        int sendCount = 10;
        delayQueueProducerConsumer.producer(sendCount);
        ThreadUtil.sleep((sendCount + 1) * 1000L);
        assertEquals(delayQueueProducerConsumer.getCounter().intValue(), 0);
    }

}