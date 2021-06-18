package com.mountain.dynamic.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.entity.Student;
import com.mountain.dynamic.entity.Teacher;
import com.mountain.dynamic.mapper.StudentMapper;
import com.mountain.dynamic.service.StudentService;
import com.mountain.dynamic.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@Service
@DS("shardingDs")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    TeacherService teacherService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveStudent() throws Exception {
        Snowflake snowflake = IdUtil.createSnowflake(0, 0);
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setId(snowflake.nextId());
            student.setName(String.valueOf(i));
            //student.setUserId(1405861487733374976L);
            student.setUserId(Long.parseLong(String.valueOf(i)));
            boolean flag = this.save(student);
            System.out.println(flag);
        }
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setId(snowflake.nextId());
            student.setName(String.valueOf(i));
            //student.setUserId(1405861487976644608L);
            student.setUserId(Long.parseLong(String.valueOf(i)));
            boolean flag = this.save(student);
            System.out.println(flag);
        }
        //throw new Exception();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void testMore() throws Exception {
        //更新只能通过分片键更新
        Snowflake snowflake = IdUtil.createSnowflake(0, 0);
        UpdateWrapper<Student> studentUpdateWrapper = new UpdateWrapper<>();
        studentUpdateWrapper.lambda().set(Student::getName, String.valueOf(snowflake.nextId()));
        studentUpdateWrapper.lambda().eq(Student::getUserId, 5L);
        boolean flag = this.update(studentUpdateWrapper);
        System.out.println(flag);

        UpdateWrapper<Teacher> teacherUpdateWrapper = new UpdateWrapper<>();
        teacherUpdateWrapper.lambda().set(Teacher::getName, String.valueOf(snowflake.nextId()));
        teacherUpdateWrapper.lambda().eq(Teacher::getUserId, 1405861487733374976L);
        boolean flag1 = teacherService.update(teacherUpdateWrapper);
        System.out.println(flag1);

        //throw new Exception();
    }

}
