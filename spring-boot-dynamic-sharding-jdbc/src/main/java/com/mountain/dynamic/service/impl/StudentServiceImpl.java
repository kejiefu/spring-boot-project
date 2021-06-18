package com.mountain.dynamic.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.entity.Student;
import com.mountain.dynamic.mapper.StudentMapper;
import com.mountain.dynamic.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


}
