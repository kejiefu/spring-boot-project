package com.mountain.dynamic.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mountain.dynamic.entity.User;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/26 18:15
 * @Created by kejiefu
 */
@DS("shardingDs")
public interface UserService extends IService<User> {

    void saveUser() throws Exception;

}
