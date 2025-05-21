package com.snail.source.code.jdbc.pool;

import com.snail.source.code.jdbc.pool.manager.ConnectionPoolManager;

import java.sql.Connection;

import static java.lang.Thread.sleep;

/**
 * @author zhangpengjun
 * @date 2024/8/22
 */
public class TestDataSource {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new ThreadConnection(), "thread-" + i).start();
        }
    }

    static class ThreadConnection implements Runnable {
        @Override
        public void run() {
            Connection connection = ConnectionPoolManager.getConnection();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ConnectionPoolManager.releaseConnection(connection);
        }
    }
}
