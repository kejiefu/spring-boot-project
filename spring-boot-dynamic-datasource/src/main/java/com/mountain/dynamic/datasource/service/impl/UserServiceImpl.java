package com.mountain.dynamic.datasource.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.datasource.entity.User;
import com.mountain.dynamic.datasource.mapper.UserMapper;
import com.mountain.dynamic.datasource.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @DS("ds2") 不加默认为主库
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@DS("ds2")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



}
