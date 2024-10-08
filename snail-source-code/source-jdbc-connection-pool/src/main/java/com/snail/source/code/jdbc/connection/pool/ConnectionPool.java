package com.snail.source.code.jdbc.connection.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接池
 *
 * @author zhangpengjun
 * @date 2024/8/21
 */
public interface ConnectionPool {

    /**
     * 获取连接
     *
     * @return {@link Connection }
     */
    Connection getConnection();

    /**
     * 释放连接
     *
     * @param conn 连接
     */
    void releaseConnection(Connection conn);

    /**
     * 关闭连接池
     */
    void shutdown() throws SQLException;

}
