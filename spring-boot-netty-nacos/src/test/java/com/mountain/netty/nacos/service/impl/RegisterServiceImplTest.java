package com.mountain.netty.nacos.service.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.mountain.netty.nacos.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/14 21:21
 * @Created by kejiefu
 */
@Slf4j
public class RegisterServiceImplTest extends ApplicationTests {

    @Test
    public void test() {
        try {
            //获取nacos服务
            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
            //将服务注册到注册中心
            namingService.registerInstance("springboot-netty", "127.0.0.1", Integer.parseInt("8848"));
        } catch (NacosException e) {
            log.error("注册nacos失败", e);
        }
    }

}