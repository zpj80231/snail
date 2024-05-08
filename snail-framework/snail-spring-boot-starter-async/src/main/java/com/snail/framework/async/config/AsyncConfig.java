package com.snail.framework.async.config;

import com.snail.framework.async.thread.ThreadPoolExecutorMdc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 配置是默认值
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Value("${spring.task.execution.pool.core-size:4}")
    private int corePoolSize;
    @Value("${spring.task.execution.pool.max-size:20}")
    private int maxPoolSize;
    @Value("${spring.task.execution.pool.keep-alive:3}")
    private int keepAliveSeconds;
    @Value("${spring.task.execution.pool.queue-capacity:2000}")
    private int queueCapacity;
    @Value("${spring.task.execution.thread-name-prefix:async-exec-}")
    private String threadNamePrefix;

    @Override
    public Executor getAsyncExecutor() {
        return createThreadPoolTaskExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, queueCapacity, threadNamePrefix);
    }

    public ThreadPoolTaskExecutor createThreadPoolTaskExecutor(String threadNamePrefix) {
        return createThreadPoolTaskExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, queueCapacity, threadNamePrefix);
    }

    public static ThreadPoolTaskExecutor createThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds,
                                                                      int queueCapacity, String threadNamePrefix) {
        ThreadPoolExecutorMdc threadPoolExecutorMdc = new ThreadPoolExecutorMdc();
        threadPoolExecutorMdc.setCorePoolSize(corePoolSize);
        threadPoolExecutorMdc.setMaxPoolSize(maxPoolSize);
        threadPoolExecutorMdc.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolExecutorMdc.setQueueCapacity(queueCapacity);
        threadPoolExecutorMdc.setThreadNamePrefix(threadNamePrefix);
        threadPoolExecutorMdc.initialize();
        return threadPoolExecutorMdc;
    }

}
