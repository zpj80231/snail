package com.snail.source.mybatis;

import com.snail.source.mybatis.entity.User;
import com.snail.source.mybatis.io.Resources;
import com.snail.source.mybatis.mapper.UserMapper;
import com.snail.source.mybatis.sessioin.SqlSession;
import com.snail.source.mybatis.sessioin.SqlSessionFactory;
import com.snail.source.mybatis.sessioin.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author zhangpj
 * @date 2025/10/28
 */
public class SnailMybatisTest {

    @Test
    public void test1() throws Exception {
        // 1. 加载配置文件到内存
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 2. 解析配置文件，封装为 Configuration 和 MapperStatement 对象
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(resourceAsStream);
        // 3. 创建数据源，创建执行器 Executor 对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4. 执行 sql，手动传入 statementId
        List<User> userList = sqlSession.selectList("user.selectList", null);
        System.out.println("指定statementId方式，selectList：" + userList);
        User user = new User(1, "张三");
        User userOne = sqlSession.selectOne("user.selectOne", user);
        System.out.println("指定statementId方式，selectOne：" + userOne);

        // 5. 关闭连接
        sqlSession.close();
    }

    @Test
    public void test2() throws Exception {
        // 1. 加载配置文件到内存
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 2. 解析配置文件，封装为 Configuration 和 MapperStatement 对象
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(resourceAsStream);
        // 3. 创建数据源，创建执行器 Executor 对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4. 执行 sql，使用 mapper 接口代理
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.selectList();
        System.out.println("mapper接口方式，selectList：" + userList);
        User user = new User(1, "张三");
        User userOne = userMapper.selectOne(user);
        System.out.println("mapper接口方式，selectOne：" + userOne);

        // 5. 关闭连接
        sqlSession.close();
    }

}
