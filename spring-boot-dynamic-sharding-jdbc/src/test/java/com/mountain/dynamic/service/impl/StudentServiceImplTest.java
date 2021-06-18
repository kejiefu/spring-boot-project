package com.mountain.dynamic.service.impl;


import com.mountain.dynamic.ApplicationTests;
import com.mountain.dynamic.service.StudentService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/6/14 15:56
 * @Created by kejiefu
 */
public class StudentServiceImplTest extends ApplicationTests {

    @Resource
    StudentService studentService;

    @Test
    public void test1() throws Exception {
        studentService.saveStudent();
    }

    @Test
    public void test2() throws Exception {
        studentService.testMore();
    }

}