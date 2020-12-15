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

    /**
     * 注入单个服务器
     */
    @Test
    public void testSingle() {
        try {
            //获取nacos服务
            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
            //将服务注册到注册中心
            //服务地址的ip
            String ip = "127.0.0.1";
            //服务地址的端口
            int port = Integer.parseInt("18088");
            namingService.registerInstance("springboot-netty", ip, port);
        } catch (NacosException e) {
            log.error("注册nacos失败", e);
        }
    }


    /**
     * 注入多个服务器
     */
    @Test
    public void testCluster() {
        try {
            //获取nacos服务
            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
            //将服务注册到注册中心
            //服务地址的ip
            String ip = "127.0.0.1";
            //服务地址的端口
            int port = Integer.parseInt("18088");
            String clusterName = "nacos_cluster";
            namingService.registerInstance("springboot-netty", ip, port, clusterName);
        } catch (NacosException e) {
            log.error("注册nacos失败", e);
        }
    }

}