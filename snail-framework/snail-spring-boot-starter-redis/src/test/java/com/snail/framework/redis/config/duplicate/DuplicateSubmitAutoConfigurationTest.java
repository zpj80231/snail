package com.snail.framework.redis.config.duplicate;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import com.snail.framework.redis.config.DuplicateSubmitAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Slf4j
@Import({RedisAutoConfiguration.class, Eagle.class})
@SpringBootTest(classes = DuplicateSubmitAutoConfiguration.class)
class DuplicateSubmitAutoConfigurationTest {

    @Autowired
    private Eagle eagle;

    /**
     * 测试重复提交
     * <p>
     * eagle.fly()：3秒内禁止重复提交请求，3秒后可以继续请求
     *
     * @throws IOException io异常
     */
    @Test
    void testDuplicateSubmit() throws IOException {
        int count = 5;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> eagle.fly());
        ThreadUtil.sleep(1000);
        log.info("--------------------------------");
        eagle.fly();
        assertEquals(1, eagle.getCount());
        ThreadUtil.sleep(3000);
        log.info("--------------------------------");
        eagle.fly();
        assertEquals(2, eagle.getCount());
        ct.close();
    }

    /**
     * 使用 args（sp el 表达式） 测试重复提交
     * <p>
     * eagle.flyCount(2)： 默认1秒内禁止重复提交请求，1秒后可以继续请求
     *
     * @throws IOException io异常
     */
    @Test
    void testDuplicateSubmitWithArgs() throws IOException {
        int count = 5;
        ConcurrencyTester ct = new ConcurrencyTester(count);
        ct.test(() -> eagle.flyCount(2));
        ThreadUtil.sleep(1000);
        log.info("--------------------------------");
        eagle.fly();
        ct.close();
        assertEquals(3, eagle.getCount());
    }

}
