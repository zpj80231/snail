package com.snail.source.mybatis.mapper;

import com.snail.source.mybatis.entity.User;

import java.util.List;

/**
 *
 * @author zhangpj
 * @date 2025/10/28
 */
public interface UserMapper {

    List<User> selectList();

    User selectOne(User user);

}
