package com.snail.framework.redis.config.lock;

import cn.hutool.core.thread.ThreadUtil;
import com.snail.framework.redis.lock.Lock;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Slf4j
public class Duck {

    private int count = 0;

    public int getCount() {
        return count;
    }

    /**
     * 所有线程阻塞式，等待获取锁，获取到锁后，执行方法，执行完毕后，释放锁
     */
    @Lock
    public void quack() {
        ThreadUtil.sleep(200);
        count ++;
        log.info("Quack!, count: {}", count);
    }

    @Lock(key = "'lock_rounds_' + #rounds")
    public void quackWithArgs(int rounds) {
        for (int i = 0; i < rounds; i++) {
            ThreadUtil.sleep(200);
            count ++;
            log.info("quackWithArgs Quack!, count: {}", count);
        }
    }

    /**
     * 当前线程，最大等待时间：WaitTime 还没有获取到锁，那么就不等锁了，会立马执行当前方法，所以可能会有并发问题（多测几次）
     */
    @Lock(waitTime = 2L, leaseTime = 7L)
    public void quackWaitTimeOut() {
        ThreadUtil.sleep(1000);
        count++;
        log.info("quackWaitTimeOut Quack!, count: {}", count);
    }

    /**
     * 当前线程获取锁后，LeaseTime 到期时，不管方法是否执行完毕，都会立即释放锁
     */
    @Lock(leaseTime = 1L)
    public void quackLeaseTimeOut() {
        ThreadUtil.sleep( 2000);
        count++;
        log.info("quackLeaseTimeOut Quack!, count: {}", count);
    }

}
