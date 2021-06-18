package com.mountain.dynamic.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.entity.User;
import com.mountain.dynamic.mapper.UserMapper;
import com.mountain.dynamic.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@Service
@DS("shardingDs")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
