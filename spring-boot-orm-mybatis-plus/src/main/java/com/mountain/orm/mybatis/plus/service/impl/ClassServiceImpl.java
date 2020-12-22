package com.mountain.orm.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.orm.mybatis.plus.entity.Class;
import com.mountain.orm.mybatis.plus.entity.User;
import com.mountain.orm.mybatis.plus.mapper.ClassMapper;
import com.mountain.orm.mybatis.plus.service.ClassService;
import com.mountain.orm.mybatis.plus.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/22 11:44
 * @Created by kejiefu
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Resource
    UserService userService;

    /**
     * 测试事务回滚
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert() {
        //插入班级
        Class class1 = new Class();
        class1.setId(System.currentTimeMillis());
        class1.setName("5年级");
        this.save(class1);
        //插入学生
        User user = new User();
        //注释事务会回滚
        //user.setId(System.currentTimeMillis());
        user.setStatus(1);
        user.setName("张三");
        user.setClassId(class1.getId());
        userService.save(user);
        return true;
    }

}
