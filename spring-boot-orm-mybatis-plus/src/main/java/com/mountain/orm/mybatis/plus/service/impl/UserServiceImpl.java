package com.mountain.orm.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.orm.mybatis.plus.entity.User;
import com.mountain.orm.mybatis.plus.mapper.UserMapper;
import com.mountain.orm.mybatis.plus.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
