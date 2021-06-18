package com.mountain.dynamic.service.impl;

import com.mountain.dynamic.ApplicationTests;
import com.mountain.dynamic.entity.User;
import com.mountain.dynamic.service.UserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/6/18 11:15
 * @Created by kejiefu
 */
public class UserServiceImplTest extends ApplicationTests {

    @Resource
    UserService userService;

    @Test
    public void test1() {
        User user = new User();
        for (int i = 0; i < 10; i++) {
            user.setName(String.valueOf(i));
        }
        userService.save(user);
    }

}