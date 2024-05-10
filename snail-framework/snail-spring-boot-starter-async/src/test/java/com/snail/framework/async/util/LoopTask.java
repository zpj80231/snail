package com.snail.framework.async.util;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 一个死循环任务，并随时可以停止，且任务全部异步执行
 *
 * @author zhangpj
 * @date 2024/05/10
 */
@Slf4j
public class LoopTask {

    public static void main(String args[]) throws Exception{
        LoopTask loopTask = new LoopTask();
        loopTask.initLoopTask();
        Thread.sleep(6000L);
        loopTask.shutdownLoopTask();
    }

    private List<ExecuteTask> executeTasks;

    /**
     * init 加载多个任务，每个任务异步开始执行，执行后将任务中的数据逻辑再异步处理
     */
    public void initLoopTask() {
        executeTasks = new ArrayList<>();
        executeTasks.add(new ExecuteTask("Task1"));
        executeTasks.add(new ExecuteTask("Task2"));
        for (final ExecuteTask executeTask : executeTasks) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    executeTask.doExecute();
                }
            }).start();
        }
    }

    /**
     * 关闭所有任务，关闭后任务不会直接结束，任务直到全部完成才会释放。
     */
    public void shutdownLoopTask() {
        if (!CollectionUtils.isEmpty(executeTasks)) {
            for (ExecuteTask executeTask : executeTasks) {
                executeTask.terminal();
            }
        }
    }
}

@Data
@Service
class Cat {
    private String catName;
    public Cat setCatName(String name) {
        this.catName = name;
        return this;
    }
}

/**
 * 执行任务
 *
 * @author zhangpj
 * @date 2024/05/10
 */
@Slf4j
class ExecuteTask {

    private final int POOL_SIZE = 3; // 线程池大小
    private final int SPLIT_SIZE = 4; // 数据拆分大小
    private String taskName;

    // 接收jvm关闭信号，实现优雅停机
    protected volatile boolean terminal = false;

    public ExecuteTask(String taskName) {
        this.taskName = taskName;
    }

    // 程序执行入口
    public void doExecute() {
        int i = 0;
        while(true) {
            log.info(taskName + ":Cycle-" + i + "-Begin");
            // 模拟获取数据
            List<Cat> datas = queryData();
            // 处理数据
            taskExecute(datas);
            log.info(taskName + ":Cycle-" + i + "-End");
            if (terminal) {
                // 只有应用关闭，才会走到这里，用于实现优雅的下线
                break;
            }
            i++;
        }
        // 回收线程池资源
        ExecutorsUtil.releaseExecutors(taskName);
    }

    // 处理数据
    private void taskExecute(List<Cat> sourceDatas) {
        if (CollectionUtils.isEmpty(sourceDatas)) {
            return;
        }
        // 将数据拆成4份
        List<List<Cat>> splitDatas = ListUtil.partition(sourceDatas, SPLIT_SIZE);
        final CountDownLatch latch = new CountDownLatch(splitDatas.size());

        // 并发处理拆分的数据，共用一个线程池
        for (final List<Cat> datas : splitDatas) {
            ExecutorService executorService = ExecutorsUtil.loadExecutors(taskName, POOL_SIZE, POOL_SIZE);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    doProcessData(datas, latch);
                }
            });
        }

        try {
            latch.await();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    // 处理单个任务数据
    private void doProcessData(List<Cat> datas, CountDownLatch latch) {
        try {
            for (Cat cat : datas) {
                log.info(taskName + ":" + cat.toString() + ",ThreadName:" + Thread.currentThread().getName());
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }

    // 优雅停机
    public void terminal() {
        // 关机
        terminal = true;
        log.info(taskName + " shut down");
    }

    // 获取永动任务数据
    private List<Cat> queryData() {
        List<Cat> datas = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            datas.add(new Cat().setCatName("罗小黑" + i));
        }
        return datas;
    }
}
