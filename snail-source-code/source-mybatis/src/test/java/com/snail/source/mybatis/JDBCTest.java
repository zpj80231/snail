package com.snail.source.mybatis;

import com.snail.source.mybatis.entity.User;
import org.junit.Test;

import java.sql.*;

/**
 *
 * @author zhangpj
 * @date 2025/10/28
 */
public class JDBCTest {

    @Test
    public void test() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1.加载驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 2.获取连接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "12345678");

            // 3.定义sql语句，创建statement
            String sql = "select * from user where name = ?";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, "张三");

            // 4.执行sql，查询出结果集
            resultSet = preparedStatement.executeQuery();
            // 遍历查询结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("name");
                // 5.根据结果集，封装User
                User user = new User();
                user.setId(id);
                user.setName(username);
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6.释放资源
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

}
