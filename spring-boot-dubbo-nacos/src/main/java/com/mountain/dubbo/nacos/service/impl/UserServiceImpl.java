package com.mountain.dubbo.nacos.service.impl;

import com.google.common.collect.Lists;
import com.mountain.dubbo.nacos.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/2/1 20:36
 * @Created by kejiefu
 */
@DubboService(version = "${service.version}")
public class UserServiceImpl implements UserService {

    @Override
    public String getUser() {
        return "ke";
    }

    @Override
    public List<String> userList() {
        List<String> stringList = Lists.newArrayList();
        stringList.add("ke");
        stringList.add("jie");
        return stringList;
    }
}
