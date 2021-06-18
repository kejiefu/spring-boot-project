package com.mountain.dynamic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mountain.dynamic.entity.Student;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/22 11:44
 * @Created by kejiefu
 */
public interface StudentService extends IService<Student> {

    void saveStudent() throws Exception;

    void testMore() throws Exception;

}

