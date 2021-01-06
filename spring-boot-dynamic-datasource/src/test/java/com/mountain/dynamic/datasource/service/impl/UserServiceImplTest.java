package com.mountain.dynamic.datasource.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mountain.dynamic.datasource.ApplicationTests;
import com.mountain.dynamic.datasource.entity.User;
import com.mountain.dynamic.datasource.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/1/6 15:16
 * @Created by kejiefu
 */
public class UserServiceImplTest extends ApplicationTests {

    @Resource
    UserService userService;

    @Test
    public void testSave() {
        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setId(RandomUtils.nextLong(1, 100000000000L));
            userService.save(user);
        }
    }

    @Test
    public void testSelect() {
        User user = userService.getById("7474400289");
        System.out.println(JSONObject.toJSONString(user));
    }

    @Test
    public void test() {

    }


}