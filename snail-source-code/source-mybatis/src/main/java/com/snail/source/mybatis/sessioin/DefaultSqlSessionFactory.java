package com.snail.source.mybatis.sessioin;

import com.snail.source.mybatis.executor.SimpleExecutor;
import com.snail.source.mybatis.pojo.Configuration;

/**
 * 连接工厂
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        // 简单执行器对象
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        // 返回 SqlSession 对象
        // SqlSession 是一层代理或拦截器，真正干活的是 Executor
        return new DefaultSqlSession(configuration, simpleExecutor);
    }

}
