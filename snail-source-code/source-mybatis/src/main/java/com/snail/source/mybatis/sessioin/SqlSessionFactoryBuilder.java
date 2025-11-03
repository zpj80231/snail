package com.snail.source.mybatis.sessioin;

import com.snail.source.mybatis.config.XMLConfigBuilder;
import com.snail.source.mybatis.pojo.Configuration;

import java.io.InputStream;

/**
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class SqlSessionFactoryBuilder {

    /**
     * 解析配置文件，创建 SqlSessionFactory 对象
     *
     * @param inputStream 输入流
     * @return {@link SqlSessionFactory }
     * @throws Exception 异常
     */
    public static SqlSessionFactory build(InputStream inputStream) throws Exception {
        // 1. 解析配置文件，创建 Configuration 核心对象
        Configuration configuration = new XMLConfigBuilder().parse(inputStream);
        // 2. 创建 SqlSessionFactory 对象
        return new DefaultSqlSessionFactory(configuration);
    }

}
