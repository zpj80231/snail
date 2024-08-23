package com.snail.source.code.jdbc.connection.pool;

import java.sql.Connection;

/**
 * @author zhangpengjun
 * @date 2024/8/21
 */
public interface IConnectionPool {

    /**
     * 获取连接
     *
     * @return {@link Connection }
     */
    public Connection getConn();

    /**
     * 释放连接
     *
     * @param conn 连接
     * @return
     */
    public boolean releaseConn(Connection conn);

}
