package com.snail.source.mybatis.executor;

import com.snail.source.mybatis.pojo.Configuration;
import com.snail.source.mybatis.pojo.MapperStatement;

import java.util.List;

/**
 * 底层 JDBC 的封装
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object param) throws Exception;

    void close();

}
