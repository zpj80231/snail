package com.snail.source.code.jdbc.connection.domain;

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
     * 使用开始时间
     */
    private long useStartTime;

}
