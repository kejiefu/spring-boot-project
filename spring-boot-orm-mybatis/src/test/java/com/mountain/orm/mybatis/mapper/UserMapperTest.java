package com.mountain.orm.mybatis.mapper;

import com.mountain.orm.mybatis.ApplicationTests;
import com.mountain.orm.mybatis.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Random;

/**
 * @Description TODO
 * @Date 2020/11/23 16:32
 * @Created by kejiefu
 */
public class UserMapperTest extends ApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testListUser() {
        userMapper.listUser();
    }

    @Test
    public void testSelectUserById() {
        userMapper.selectUserById(1L);
    }

    @Test
    public void testSaveUser() {
        Random random = new Random();
        User user = new User();
        user.setId(random.nextLong());
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        user.setName("fu");
        user.setStatus(0);
        userMapper.saveUser(user);
    }

    @Test
    public void testDeleteById() {
        userMapper.deleteById(1L);
    }


}