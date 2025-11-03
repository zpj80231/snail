package com.snail.source.mybatis.sessioin;

import java.util.List;

/**
 * SqlSession 数据库操作接口
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public interface SqlSession {

    /**
     * 查询多条
     *
     * @param statementId 语句 ID
     * @param param       查询参数
     * @return {@link List }<{@link E }>
     */
    <E> List<E> selectList(String statementId, Object param) throws Exception;

    /**
     * 查询单条
     *
     * @param statementId 语句 ID
     * @param param       参数
     * @return {@link T }
     */
    <T> T selectOne(String statementId, Object param) throws Exception;

    /**
     * 关闭资源
     *
     */
    void close();

    /**
     * 获取 Mapper 代理对象
     *
     * @param mapperClass 映射器类
     * @return {@link T }
     */
    <T> T getMapper(Class<T> mapperClass);

}
