package com.snail.framework.redis.delay;

import com.snail.framework.async.util.ExecutorsUtil;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 延迟消息消费器容器启动器。用于监听延迟队列中的消息，并将其传递给消费者进行处理。
 * 该类在 Spring 容器初始化后自动启动，并定期检查监听线程状态，确保线程始终运行。
 *
 * @author zhangpengjun
 * @date 2024/5/8
 */
@Slf4j
public class DelayMessageConsumerContainerLauncher implements InitializingBean {

    /**
     * 创建一个线程池用于执行队列中的任务
     */
    private static final ExecutorService THREAD_POOL = ExecutorsUtil.loadExecutors("delayed-queue-", 4, 20);
    /**
     * 创建一个单线程的调度程序用于定期检查线程状态
     */
    private static final ScheduledExecutorService SCHEDULER_POOL = Executors.newSingleThreadScheduledExecutor();

    /**
     * 延迟队列实例
     */
    private final DelayQueue delayQueue;
    /**
     * 死信队列消费者实例
     */
    private final DelayMessageDeadLetterConsumer delayMessageDeadLetterConsumer;

    /**
     * 构造函数，用于初始化延迟队列和死信队列消费者。
     *
     * @param delayQueue 延迟队列
     * @param delayMessageDeadLetterConsumer 死信队列消费者
     */
    public DelayMessageConsumerContainerLauncher(DelayQueue delayQueue, DelayMessageDeadLetterConsumer delayMessageDeadLetterConsumer) {
        this.delayQueue = delayQueue;
        this.delayMessageDeadLetterConsumer = delayMessageDeadLetterConsumer;
    }

    /**
     * 启动方法，负责启动所有队列的监听线程，并启动监控线程。
     */
    public void start() {
        // 遍历所有消费者容器，启动并监控每个队列的监听线程
        delayQueue.getConsumerContainers().forEach(this::startThreadWithScheduledMonitoring);
    }

    /**
     * 启动并监控线程获取队列。
     *
     * @param queueName 队列名称
     * @param consumerContainer 消费者容器
     */
    private void startThreadWithScheduledMonitoring(String queueName, DelayMessageConsumerContainer consumerContainer) {
        // 设置定时任务，每隔10秒检查一次线程状态
        SCHEDULER_POOL.scheduleAtFixedRate(() -> {
            if (!isThreadAlive(queueName)) {
                startListening(queueName, consumerContainer);
            }
        }, 0, 10, TimeUnit.SECONDS); // 检查间隔为 10 秒
    }

    /**
     * 启动监听线程，监听指定队列，并在队列有元素时将任务提交给线程池处理。
     *
     * @param queueName 队列名称
     * @param consumerContainer 消费者容器
     */
    private void startListening(String queueName, DelayMessageConsumerContainer consumerContainer) {
        Thread listenerThread = new Thread(() -> {
            log.info("Start the listening delay queue thread: {}", queueName);
            while (true) {
                try {
                    // 从延迟队列中获取消息
                    DelayMessage<Object> message = delayQueue.take(queueName);
                    // 将消息提交到线程池处理
                    THREAD_POOL.execute(() -> {
                        try {
                            consumerContainer.invoke(message);
                        } catch (Exception e) {
                            // 如果处理消息时出现异常，将消息传递到死信队列消费者进行处理
                            delayMessageDeadLetterConsumer.receivedMessage(message, e);
                        }
                    });
                } catch (InterruptedException e) {
                    log.error("监听队列线程被中断", e);
                    Thread.currentThread().interrupt();
                    break; // 退出循环并结束线程
                } catch (Exception e) {
                    log.error("监听队列线程出现意外错误", e);
                }
            }
        });
        listenerThread.setName(queueName);
        listenerThread.start();
    }

    /**
     * 检查线程是否存活。
     *
     * @param threadName 线程名称
     * @return 线程是否存活
     */
    private boolean isThreadAlive(String threadName) {
        // 遍历所有线程，检查指定名称的线程是否存活
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) {
                return t.isAlive();
            }
        }
        return false;
    }

    /**
     * 在 Spring 容器初始化后自动调用，启动队列监听线程。
     */
    @Override
    public void afterPropertiesSet() {
        start();
    }
}
