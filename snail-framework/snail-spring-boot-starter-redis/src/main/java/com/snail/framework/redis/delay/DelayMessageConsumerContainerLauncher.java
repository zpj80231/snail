package com.snail.framework.redis.delay;

import com.snail.framework.async.util.ExecutorsUtil;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;

/**
 * @author zhangpengjun
 * @date 2024/5/8
 */
@Slf4j
public class DelayMessageConsumerContainerLauncher implements InitializingBean {

    private static final ExecutorService THREAD_POOL = ExecutorsUtil.loadExecutors("delayed-queue-", 4, 20);

    private final DelayQueue delayQueue;
    private final DelayMessageDeadLetterConsumer delayMessageDeadLetterConsumer;

    public DelayMessageConsumerContainerLauncher(DelayQueue delayQueue, DelayMessageDeadLetterConsumer delayMessageDeadLetterConsumer) {
        this.delayQueue = delayQueue;
        this.delayMessageDeadLetterConsumer = delayMessageDeadLetterConsumer;
    }

    public void start() {
        // 由于此线程需要常驻，可以新建线程，不用交给线程池管理
        delayQueue.getConsumerContainers().forEach((queueName, consumerContainer) -> {
            Thread thread = new Thread(() -> {
                log.info("Start the listening delay queue thread:{}", queueName);
                while (true) {
                    try {
                        DelayMessage<Object> message = delayQueue.take(queueName);
                        THREAD_POOL.execute(() -> {
                            try {
                                consumerContainer.invoke(message);
                            } catch (Exception e) {
                                delayMessageDeadLetterConsumer.receivedMessage(message);
                                log.error("队列线程处理异常", e);
                            }
                        });
                    } catch (InterruptedException e) {
                        log.error("监听队列线程被中断", e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.setName(queueName);
            thread.start();
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
