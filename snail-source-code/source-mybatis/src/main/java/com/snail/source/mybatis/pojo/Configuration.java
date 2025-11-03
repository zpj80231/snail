package com.snail.source.mybatis.pojo;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装 mybatis 配置信息
 *
 * @author zhangpj
 * @date 2025/10/27
 */
@Data
public class Configuration {

    /**
     * 数据源
     */
    private DataSource dataSource;
    /**
     * mapper statementId 语句映射集合
     */
    private Map<String, MapperStatement> mapperStatementMap = new HashMap<>();

}
