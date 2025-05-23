package com.snail.source.code.jdbc.pool.manager;

import com.snail.source.code.jdbc.pool.properties.DataSourceProperties;
import com.snail.source.code.jdbc.pool.support.ConnectionPool;
import com.snail.source.code.jdbc.pool.support.SimpleConnectionPool;
import lombok.NoArgsConstructor;

import java.sql.Connection;

/**
 * @author zhangpengjun
 * @date 2024/8/22
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ConnectionPoolManager {

    private static final DataSourceProperties config = new DataSourceProperties();
    private static final ConnectionPool CONNECTION_POOL = new SimpleConnectionPool(config);

    /**
     * 获取连接(重复利用机制)
     *
     * @return {@link Connection }
     */
    public static Connection getConnection() {
        return CONNECTION_POOL.getConnection();
    }

    /**
     * 释放连接(可回收机制)
     *
     * @param connection 连接
     */
    public static void releaseConnection(Connection connection) {
        CONNECTION_POOL.releaseConnection(connection);
    }

}
