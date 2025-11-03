package com.snail.source.mybatis.executor;

import com.snail.source.mybatis.pojo.Configuration;
import com.snail.source.mybatis.pojo.MapperStatement;
import com.snail.source.mybatis.utils.GenericTokenParser;
import com.snail.source.mybatis.utils.ParameterMapping;
import com.snail.source.mybatis.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单执行器
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class SimpleExecutor implements Executor {

    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement = null;

    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object param)
            throws Exception {
        // 1. 加载驱动，获取连接
        connection = configuration.getDataSource().getConnection();

        // 2. 转换 sql，创建 PreparedStatement
        // 获取 mapper sql 模版
        // 如：select * from user where id = #{id} and username = #{username}
        String sql = mapperStatement.getSql();
        String parameterType = mapperStatement.getParameterType();
        // 解析 sql 模版，将 #{id} 替换为 ?
        BoundSql boundSql = getBoundSql(sql);
        String finalSql = boundSql.getFinalSql();
        preparedStatement = connection.prepareStatement(finalSql);

        // 3. 参数赋值
        if (parameterType != null && param != null) {
            Class<?> parameterTypeClass = Class.forName(parameterType);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                // 获取参数字段名
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String paramName = parameterMapping.getContent();
                // 获取参数值
                Field field = parameterTypeClass.getDeclaredField(paramName);
                field.setAccessible(true);
                Object paramValue = field.get(param);
                preparedStatement.setObject(i + 1, paramValue);
            }
        }

        // 4. 执行 sql
        resultSet = preparedStatement.executeQuery();

        // 5. 解析结果集
        List<E> list = new ArrayList<>();
        while (resultSet.next()) {
            // mapper 模板标签的返回值类型
            String resultType = mapperStatement.getResultType();
            Class<?> resultTypeClass = Class.forName(resultType);
            Object obj = resultTypeClass.newInstance();

            // 数据库中的结果集
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(columnName);
                // columnName：数据库字段名，而java实体类中字段名可能与数据库字段名不一致，所以需要一个转换
                // 可以获取实体类的 get、set 方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(obj, columnValue);
            }
            list.add((E) obj);
        }

        return list;
    }

    /**
     * 获取绑定SQL及对应的参数
     * 1. 解析 mapper sql 模板，将 #{字段} 替换为 ?，最终赋值为 finalSql
     * 2. 获取参数映射。将 #{} 解析后的字段名，封装为 ParameterMapping 对象，并保存在 List<ParameterMapping> 中
     *
     * @param sql mapper sql
     * @return {@link BoundSql }
     */
    private BoundSql getBoundSql(String sql) {
        // 1. 标记处理器
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        // 2. 标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 3. 解析器开始解析 sql
        // #{} 占位符替换成 ？
        // #{} 的字段名也会保存在标记处理器中
        String finalSql = genericTokenParser.parse(sql);
        // 4. 从标记处理器获取参数映射真实值
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(finalSql, parameterMappings);
    }

    @Override
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}