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
     * 初始化
     *
     * @param poolName        池名称
     * @param corePoolSize    核心池大小
     * @param maximumPoolSize 最大池大小
     * @return {@link ExecutorService }
     */
    private static ExecutorService init(String poolName, int corePoolSize, int maximumPoolSize) {
        int keepAliveTime = corePoolSize == maximumPoolSize ? 0 : 60;
        return init(poolName, corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS);
    }

    /**
     * 初始化一个线程池
     *
     * @param poolName        池名称
     * @param corePoolSize    核心池大小
     * @param maximumPoolSize 最大池大小
     * @param keepAliveTime   保持活力时间
     * @param unit            单位
     * @return {@link ExecutorService }
     */
    private static ExecutorService init(String poolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        return new ThreadPoolExecutorMdc(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("Pool-" + poolName).setDaemon(false).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 加载线程池，没有的话自动创建
     *
     * @param poolName        池名称
     * @param corePoolSize    核心池大小
     * @param maximumPoolSize 最大池大小
     * @return {@link ExecutorService }
     */
    public static ExecutorService loadExecutors(String poolName, int corePoolSize, int maximumPoolSize) {
        ExecutorService executorService = executors.get(poolName);
        if (null == executorService) {
            synchronized (ExecutorsUtil.class) {
                executorService = executors.get(poolName);
                if (null == executorService) {
                    executorService = init(poolName, corePoolSize, maximumPoolSize);
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