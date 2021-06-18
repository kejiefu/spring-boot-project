package com.mountain.dynamic.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.entity.Teacher;
import com.mountain.dynamic.mapper.TeacherMapper;
import com.mountain.dynamic.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveTeacher() throws Exception {
        Snowflake snowflake = IdUtil.createSnowflake(0, 0);
        for (int i = 0; i < 2; i++) {
            Teacher teacher = new Teacher();
            teacher.setId(snowflake.nextId());
            teacher.setName(String.valueOf(i));
            teacher.setUserId(1405861487733374976L);
            boolean flag = this.save(teacher);
            System.out.println(flag);
        }
        for (int i = 0; i < 2; i++) {
            Teacher teacher = new Teacher();
            teacher.setId(snowflake.nextId());
            teacher.setName(String.valueOf(i));
            teacher.setUserId(1405861487976644608L);
            boolean flag = this.save(teacher);
            System.out.println(flag);
        }
        //throw new Exception();
    }

}
