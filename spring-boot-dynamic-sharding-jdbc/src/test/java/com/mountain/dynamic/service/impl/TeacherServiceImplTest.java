package com.mountain.dynamic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mountain.dynamic.ApplicationTests;
import com.mountain.dynamic.entity.Teacher;
import com.mountain.dynamic.entity.User;
import com.mountain.dynamic.service.TeacherService;
import com.mountain.dynamic.service.UserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/6/18 19:57
 * @Created by kejiefu
 */
public class TeacherServiceImplTest extends ApplicationTests {

    @Resource
    TeacherService teacherService;

    @Resource
    UserService userService;

    @Test
    public void test1() throws Exception {
        teacherService.saveTeacher();
    }

    @Test
    public void test2() throws Exception {
        User user = userService.getById(1405861484948357120L);
        System.out.println(JSONObject.toJSONString(user));
        Teacher teacher = teacherService.getById(1405863748039606272L);
        System.out.println(JSONObject.toJSONString(teacher));
    }

}