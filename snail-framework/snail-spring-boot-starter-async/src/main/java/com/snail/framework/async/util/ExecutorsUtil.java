package com.snail.framework.async.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.snail.framework.async.thread.ThreadPoolExecutorMdc;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Executors util
 *
 * @author zhangpj
 * @date 2024/05/10
 */
public class ExecutorsUtil {

    /**
     * 每个任务，都有自己单独的线程池
     */
    private static final Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

    /**
     * 初始化一个线程池
     *
     * @param poolName 池名称
     * @param poolSize 池大小
     * @return {@link ExecutorService }
     */
    private static ExecutorService init(String poolName, int poolSize) {
        return new ThreadPoolExecutorMdc(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("Pool-" + poolName).setDaemon(false).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 加载线程池，没有的话自动创建
     *
     * @param poolName 池名称
     * @param poolSize 池大小
     * @return {@link ExecutorService }
     */
    public static ExecutorService loadExecutors(String poolName, int poolSize) {
        ExecutorService executorService = executors.get(poolName);
        if (null == executorService) {
            synchronized (ExecutorsUtil.class) {
                executorService = executors.get(poolName);
                if (null == executorService) {
                    executorService = init(poolName, poolSize);
                    executors.put(poolName, executorService);
                }
            }
        }
        return executorService;
    }

    /**
     * 释放线程池
     *
     * @param poolName 池名称
     */
    public static void releaseExecutors(String poolName) {
        ExecutorService executorService = executors.remove(poolName);
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}