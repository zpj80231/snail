package com.snail.source.code.jdbc.connection.pool;

import com.snail.source.code.jdbc.connection.domain.PoolEntry;
import com.snail.source.code.jdbc.connection.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangpengjun
 * @date 2024/8/21
 */
@Slf4j
public class SimpleConnectionPool implements ConnectionPool {

    /**
     * 连接池配置
     */
    DataSourceProperties config;
    /**
     * 当前连接数
     */
    private final AtomicInteger currentConnectCount = new AtomicInteger(0);
    /**
     * 空闲中的连接池
     */
    List<Connection> freePools = new CopyOnWriteArrayList<>();
    /**
     * 正在使用中的连接池
     */
    List<PoolEntry> usedPools = new CopyOnWriteArrayList<>();
    /**
     * 调度器
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // 设置为守护线程
        return thread;
    });

    public SimpleConnectionPool(DataSourceProperties config) {
        this.config = config;
        initializePool();
    }

    @Override
    public synchronized Connection getConnection() {
        Connection conn = null;
        if (usedPools.size() < config.getMaxSize()) {
            // 优先从空闲连接池获取连接，空闲连接池满的情况下判断是否达到最大连接数，没达到最大连接数则继续创建连接
            String connectionFrom;
            if (!freePools.isEmpty()) {
                conn = freePools.get(0);
                freePools.remove(conn);
                connectionFrom = "从空闲连接池获取连接";
            } else {
                // 空闲连接池已满，未达到最大连接数，新建连接
                conn = createConn();
                connectionFrom = "空闲连接池已满，未达到最大连接数，新建连接";
            }
            // 对连接进行校验，通过就放入活跃连接池
            if (isAlive(conn)) {
                usedPools.add(new PoolEntry(conn, true, System.currentTimeMillis()));
                log.info("[{}]，当前连接数：{}，最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                        connectionFrom, currentConnectCount.get(), config.getMaxSize(), freePools.size(), usedPools.size());
            } else {
                return getConnection();
            }
        } else {
            // 空闲连接池已满，达到最大连接数，等待后重试
            try {
                this.wait(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            log.info("[空闲连接池已满，达到最大连接数]，等待后重试。当前连接数：{}，最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                    currentConnectCount.get(), config.getMaxSize(), freePools.size(), usedPools.size());
        }
        return conn;
    }

    @Override
    public synchronized void releaseConnection(Connection conn) {
        if (!isAlive(conn)) {
            return;
        }
        // 从活跃连接池移除
        for (PoolEntry entry : usedPools) {
            if (entry.getConnection() == conn) {
                usedPools.remove(entry);
                break;
            }
        }
        // 空闲连接池没满，则归还到空闲连接池，否则关闭连接
        if (freePools.size() < config.getMaxSize()) {
            freePools.add(conn);
            log.info("[归还连接到空闲连接池]，当前连接数：{}，最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                    currentConnectCount.get(), config.getMaxSize(), freePools.size(), usedPools.size());
        } else {
            // 关闭连接
            try {
                conn.close();
                currentConnectCount.decrementAndGet();
            } catch (SQLException e) {
                log.error("关闭连接失败", e);
            }
        }
        notifyAll();
    }

    @Override
    public synchronized void shutdown() throws SQLException {
        // 关闭活跃连接
        for (PoolEntry entry : usedPools) {
            releaseConnection(entry.getConnection());
        }
        // 关闭空闲连接
        for (Connection conn : freePools) {
            conn.close();
            currentConnectCount.decrementAndGet();
        }
    }

    /**
     * 创建连接
     *
     * @return {@link Connection }
     */
    private synchronized Connection createConn(){
        Connection conn = null ;
        try {
            conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            currentConnectCount.incrementAndGet();
        } catch (SQLException e) {
            log.error("创建连接失败", e);
        }
        return conn;
    }

    /**
     * 校验是否有效
     *
     * @param conn 连接
     * @return boolean
     */
    private boolean isAlive(Connection conn) {
        try {
            return conn != null && conn.isValid(1000);
        } catch (SQLException e) {
            log.error("校验连接是否有效失败", e);
        }
        return false;
    }

    /**
     * 初始化
     * <br>
     * 根据配置加载连接到默认的空闲连接池
     */
    private void initializePool() {
        // 获取连接池 initSize 配置，获取连接加载到空闲连接池
        try {
            Class.forName(config.getDriver());
            for (int i = 0; i < config.getInitSize(); i++) {
                Connection conn = createConn();
                freePools.add(conn);
            }
            log.info("初始化连接池，当前连接数：{}，初始化连接数：{}, 最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                    currentConnectCount.get(), config.getInitSize(), config.getMaxSize(), freePools.size(), usedPools.size());
        } catch (ClassNotFoundException e) {
            log.error("加载驱动失败", e);
            throw new RuntimeException(e);
        }
        check();
    }

    /**
     * 开启定时任务检测连接
     * 1. 定时检查空闲连接池：检查连接是否可用，如果不可用，则关闭连接，并从空闲连接池中移除
     * 2. 定时检查正在使用的连接池：检查连接是否超时，如果超时，则关闭连接，并从正在使用的连接池中移除
     * 3. 定时检查最小连接数：如果空闲连接池小于最小连接数，则创建连接加载到空闲连接池
     */
    private void check() {
        if (Boolean.TRUE.equals(config.getHealth())) {
            scheduler.scheduleAtFixedRate(new CheckTask(), config.getDelay(), config.getPeriod(), TimeUnit.MILLISECONDS);
        }
    }

    class CheckTask extends TimerTask {
        @Override
        public void run() {
            // 1. 定时检查空闲连接池：检查连接是否可用，如果不可用，则关闭连接，并从空闲连接池中移除
            for (Connection conn : freePools) {
                if (!isAlive(conn)) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.error("检测到连接不可用，关闭连接失败", e);
                    }
                    freePools.remove(conn);
                    currentConnectCount.decrementAndGet();
                    log.info("检测到连接不可用，关闭连接，当前连接数：{}，最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                            currentConnectCount.get(), config.getMaxSize(), freePools.size(), usedPools.size());
                }
            }
            // 2. 定时检查正在使用的连接池：检查连接是否超时，如果超时，则关闭连接，并从正在使用的连接池中移除
            for (PoolEntry poolEntry : usedPools) {
                if (System.currentTimeMillis() - poolEntry.getLastUsed() > config.getTimeout()) {
                    try {
                        poolEntry.getConnection().close();
                        usedPools.remove(poolEntry);
                        currentConnectCount.decrementAndGet();
                        log.info("检测到连接超时，关闭连接，当前连接数：{}，最大连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                                currentConnectCount.get(), config.getMaxSize(), freePools.size(), usedPools.size());
                    } catch (SQLException e) {
                        log.error("检测到连接超时，关闭连接失败", e);
                    }
                }
            }
            // 3. 定时检查最小连接数：如果空闲连接池小于最小连接数，则创建连接加载到空闲连接池
            while (freePools.size() < config.getInitSize()) {
                Connection conn = createConn();
                if (conn != null) {
                    freePools.add(conn);
                    log.info("检测到空闲连接池小于最小连接数，创建连接加载到空闲连接池，当前连接数：{}，最小连接数：{}，空闲连接池：{}，正在使用的连接池：{}",
                            currentConnectCount.get(), config.getInitSize(), freePools.size(), usedPools.size());
                }
            }
            // 4. 检查最小连接数、超过最小连接数则检查最大空闲时间，大于指定时间则删除连接

        }
    }

}
