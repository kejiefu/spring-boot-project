package com.mountain.dynamic.service.impl;

import com.mountain.dynamic.ApplicationTests;
import com.mountain.dynamic.service.TeacherService;
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

    @Test
    public void test1() throws Exception {
        teacherService.saveTeacher();
    }


}