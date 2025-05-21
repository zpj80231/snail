package com.snail.source.code.jdbc.pool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;

/**
 * @author zhangpengjun
 * @date 2024/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolEntry {

    /**
     * 连接
     */
    private Connection connection;
    /**
     * 是否使用中
     */
    private boolean inUse;
    /**
     * 最后使用时间
     */
    private long lastUsed;

}
