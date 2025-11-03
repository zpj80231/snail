package com.snail.source.mybatis.pojo;

import lombok.Data;

/**
 * 封装 Mapper.xml 中的 statement 标签信息
 *
 * @author zhangpj
 * @date 2025/10/27
 */
@Data
public class MapperStatement {

    /**
     * SQL 语句 唯一ID：namespace.id
     */
    private String statementId;
    /**
     * 返回值类型
     */
    private String resultType;
    /**
     * 参数类型
     */
    private String parameterType;
    /**
     * SQL 语句
     */
    private String sql;

    /**
     * SQL 命令类型（增删改查）
     */
    private String sqlCommandType;

}
