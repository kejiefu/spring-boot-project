package com.mountain.orm.mybatis.plus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mountain.orm.mybatis.plus.ApplicationTests;
import com.mountain.orm.mybatis.plus.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:22
 * @Created by kejiefu
 */
@Slf4j
public class UserServiceTest extends ApplicationTests {

    @Autowired
    private UserService userService;

    /**
     * 测试Mybatis-Plus 新增
     */
    @Test
    public void testSave() {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(String.valueOf(System.currentTimeMillis()));
        user.setStatus(0);
        boolean flag = userService.save(user);
        log.info("flag:{}", flag);
    }

    /**
     * 测试Mybatis-Plus 批量修改插入
     */
    @Test
    public void testSaveOrUpdateBatch() {
        List<User> userList = Lists.newArrayListWithExpectedSize(10);
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(String.valueOf(System.currentTimeMillis()));
        user.setStatus(0);
        userList.add(user);
        boolean flag = userService.saveOrUpdateBatch(userList);
        log.info("flag:{}", flag);
    }

    /**
     * 测试Mybatis-Plus 查询list
     */
    @Test
    public void testList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, 1);
        List<User> userList = userService.list(queryWrapper);
        log.info("flag:{}", userList);
    }

}