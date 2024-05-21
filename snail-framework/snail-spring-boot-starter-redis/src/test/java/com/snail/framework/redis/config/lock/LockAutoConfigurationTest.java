package com.snail.framework.redis.config.lock;

import cn.hutool.core.thread.ConcurrencyTester;
import com.snail.framework.redis.config.LockAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@Import({RedissonAutoConfiguration.class, Duck.class})
@SpringBootTest(classes = {LockAutoConfiguration.class})
class LockAutoConfigurationTest {

    @Autowired
    private Duck duck;

    /**
     * 所有线程阻塞式，等待获取锁，获取到锁后，执行方法，执行完毕后，释放锁
     *
     * @throws IOException io异常
     */
    @Test
    void testLock() throws IOException {
        int count = 10;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> duck.quack());
        ct.close();
        assertEquals(count, duck.getCount());
    }

    /**
     * 所有线程阻塞式，等待获取锁，获取到锁后，执行方法，执行完毕后，释放锁
     *
     * @throws IOException io异常
     */
    @Test
    void testLockWithArgs() throws IOException {
        int count = 10;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> duck.quackWithArgs(2));
        ct.close();
        assertEquals(count * 2, duck.getCount());
    }

    /**
     * 当前线程，最大等待时间：WaitTime 还没有获取到锁，那么就不等锁了，会立马执行当前方法，所以可能会有并发问题（多测几次）
     *
     * @throws IOException io异常
     */
    @Test
    void testLockWaitTimeOut() throws IOException {
        int count = 20;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> duck.quackWaitTimeOut());
        ct.close();
        assertNotEquals(count, duck.getCount());
    }

    /**
     * 当前线程获取锁后，LeaseTime 到期时，不管方法是否执行完毕，都会立即释放锁
     *
     * @throws IOException io异常
     */
    @Test
    void testLockLeaseTimeOut() throws IOException {
        int count = 10;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> duck.quackLeaseTimeOut());
        ct.close();
        assertEquals(count, duck.getCount());
    }
}
