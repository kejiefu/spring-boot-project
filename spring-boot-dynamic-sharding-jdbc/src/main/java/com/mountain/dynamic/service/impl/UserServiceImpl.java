package com.mountain.dynamic.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mountain.dynamic.entity.User;
import com.mountain.dynamic.mapper.UserMapper;
import com.mountain.dynamic.service.UserService;
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
@DS("shardingDs")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveUser() throws Exception {
        Snowflake snowflake = IdUtil.createSnowflake(0, 0);
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(snowflake.nextId());
            user.setName(String.valueOf(i));
            boolean flag = this.save(user);
            System.out.println(flag);
        }
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(snowflake.nextId());
            user.setName(String.valueOf(i));
            boolean flag = this.save(user);
            System.out.println(flag);
        }
        //throw new Exception();
    }

}
