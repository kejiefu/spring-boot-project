package com.mountain.dubbo.nacos.service.impl;

import com.mountain.dubbo.nacos.ApplicationTests;
import com.mountain.dubbo.nacos.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import java.util.List;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/2/1 21:12
 * @Created by kejiefu
 */
public class UserServiceImplTest extends ApplicationTests {

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:20880")
    UserService userService;

    @Test
    public void getUser() {
        String user = userService.getUser();
        System.out.println(user);
    }

    @Test
    public void userList() {
        List<String> userList = userService.userList();
        System.out.println(userList);
    }
}