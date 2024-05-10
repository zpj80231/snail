package com.snail.framework.async.thread;

import lombok.NonNull;
import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 * 线程池 mdc
 *
 * @author zhangpengjun
 * @date 2022/7/18
 */
public class ThreadPoolExecutorMdc extends ThreadPoolExecutor {

    public ThreadPoolExecutorMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolExecutorMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolExecutorMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolExecutorMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        super.execute(ThreadPoolMdcFilter.wrap(runnable, MDC.getCopyOfContextMap()));
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable runnable) {
        return super.submit(ThreadPoolMdcFilter.wrap(runnable, MDC.getCopyOfContextMap()));
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> callable) {
        return super.submit(ThreadPoolMdcFilter.wrap(callable, MDC.getCopyOfContextMap()));
    }

}
