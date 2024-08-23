package com.snail.source.code.jdbc.connection.manager;

import com.snail.source.code.jdbc.connection.pool.ConnectionPool;
import com.snail.source.code.jdbc.connection.properties.DataSourceProperties;

import java.sql.Connection;

/**
 * @author zhangpengjun
 * @date 2024/8/22
 */
public class ConnectionPoolManager {

    private static final DataSourceProperties config = new DataSourceProperties();
    private static final ConnectionPool connectionPool = new ConnectionPool(config);

    /**
     * 获取连接(重复利用机制)
     *
     * @return {@link Connection }
     */
    public static Connection getConnection() {
        return connectionPool.getConn();
    }

    /**
     * 释放连接(可回收机制)
     *
     * @param connection 连接
     */
    public static void releaseConnection(Connection connection) {
        connectionPool.releaseConn(connection);
    }

}
