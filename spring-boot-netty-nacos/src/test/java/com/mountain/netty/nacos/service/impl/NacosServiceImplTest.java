package com.mountain.netty.nacos.service.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.mountain.netty.nacos.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/14 21:21
 * @Created by kejiefu
 */
@Slf4j
public class NacosServiceImplTest extends ApplicationTests {

    /**
     * 注入单个服务器
     */
    @Test
    public void testSingle() throws InterruptedException {
        try {
            //获取nacos服务
            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
            //将服务注册到注册中心
            //服务地址的ip
            String ip = "127.0.0.1";
            //服务地址的端口
            int port = Integer.parseInt("18088");
            namingService.registerInstance("springboot-netty", ip, port);
            //注册多一个，测试事例
            namingService.registerInstance("springboot-netty", ip, Integer.parseInt("8848"));
        } catch (NacosException e) {
            log.error("注册nacos失败", e);
        }
        while (true) {
            Thread.sleep(5000);
            System.out.println(System.currentTimeMillis());
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

    /**
     * 获取服务
     */
    @Test
    public void testGet() throws NacosException {
        NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
        List<Instance> instanceList = namingService.getAllInstances("springboot-netty");
        System.out.println(instanceList);
    }

}