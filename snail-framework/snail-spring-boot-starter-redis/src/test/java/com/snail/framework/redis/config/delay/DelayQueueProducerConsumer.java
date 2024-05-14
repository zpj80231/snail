package com.snail.framework.redis.config.delay;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.delay.DelayQueue;
import com.snail.framework.redis.delay.annotation.DelayQueueListener;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangpengjun
 * @date 2024/5/14
 */
@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class DelayQueueProducerConsumer {

    public static final String QUEUE_NAME_1 = "delay_queue_test_1";
    public static final String QUEUE_NAME_2 = "delay_queue_test_2";

    private final DelayQueue delayQueue;

    public AtomicInteger counter = new AtomicInteger(0);

    public void producer(int count) {
        for (int i = 1; i <= count; i++) {
            // 向 队列1 发送延迟消息
            DelayMessage<Object> delayMessage = new DelayMessage<>(Collections.singletonList(QUEUE_NAME_1));
            delayMessage.setHeader(null);
            delayMessage.setMessageId(IdUtil.getSnowflakeNextIdStr());
            delayMessage.setDelay(RandomUtil.randomLong(2, 10));
            delayMessage.setTimeUnit(TimeUnit.SECONDS);
            // 模拟各种格式的消息体
            String messageBody = "default" + i;
            delayMessage.setBody(messageBody);
            if (i % 3 == 0) {
                delayMessage.setBody(MapUtil.of("key" + i, messageBody));
            }
            if (i % 3 == 1) {
                delayMessage.setBody(new Cat("Tom" + i, i));
            }
            if (i % 3 == 2) {
                delayMessage.setBody(JSON.toJSONString(delayMessage));
            }

            log.info("Send delayMessage: {}", JSON.toJSONString(delayMessage));
            delayQueue.offer(delayMessage);
            counter.incrementAndGet();
        }

        // 向 队列2 发送一个空消息
        DelayMessage<Object> delayMessage2 = new DelayMessage<>(Collections.singletonList(QUEUE_NAME_2));
        delayQueue.offer(delayMessage2);
        log.info("Send delayMessage2: {}", JSON.toJSONString(delayMessage2));
        counter.incrementAndGet();

        // 没有绑定消费队列时，打印告警日志，不发送消息
        delayQueue.offer(new DelayMessage<>(Collections.singletonList(QUEUE_NAME_1 + "no_consumer")));
    }

    @DelayQueueListener({QUEUE_NAME_1, QUEUE_NAME_2})
    public void consumer(DelayMessage<Object> message) {
        log.info("Received message: {}", JSON.toJSONString(message));
        Object body = message.getBody();
        if (body instanceof Cat) {
            Cat cat = (Cat) body;
            log.info("cat --> name: {}, age: {}", cat.getName(), cat.getAge());
        }
        counter.decrementAndGet();
    }

}
