package com.snail.source.mybatis.sessioin;

import com.snail.source.mybatis.executor.Executor;
import com.snail.source.mybatis.pojo.Configuration;
import com.snail.source.mybatis.pojo.MapperStatement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 连接
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object param) throws Exception {
        // 根据 statementId 获取 mapper sql 语句配置
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        // 将查询（数据源、sql、参数）委托给 Executor 执行器
        return executor.query(configuration, mapperStatement, param);
    }

    @Override
    public <T> T selectOne(String statementId, Object param) throws Exception {
        List<Object> list = selectList(statementId, param);
        if (list.size() == 1) {
            return (T) list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        executor.close();
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        // 使用 JDK 动态代理生成代理对象
        Object proxyInstance = Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, (proxy, method, args) -> {
            // 1. 约定 statementId：接口名 + 方法名
            String className = mapperClass.getName();
            String methodName = method.getName();
            String statementId = className + "." + methodName;

            // 2. 获取 statementId 对应的 mapper sql 语句
            MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
            String sqlCommandType = mapperStatement.getSqlCommandType();

            // 3. 根据 sqlCommandType 执行对应的操作
            switch (sqlCommandType) {
                case "select":
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        if (args != null) {
                            return selectList(statementId, args[0]);
                        }
                        return selectList(statementId, null);
                    }
                    return selectOne(statementId, args[0]);
                case "insert":
                    // 执行新增
                    break;
                case "update":
                    // 执行修改
                    break;
                case "delete":
                    // 执行删除
                    break;
                default:
            }
            return null;
        });
        return (T) proxyInstance;
    }

}
