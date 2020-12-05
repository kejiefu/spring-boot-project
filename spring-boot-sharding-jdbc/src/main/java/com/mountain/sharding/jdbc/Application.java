package com.mountain.sharding.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @package: com.xkcoding.sharding.jdbc
 * @description: 启动器
 * @author: yangkai.shen
 * @date: Created in 2019-01-23 22:05
 * @copyright: Copyright (c) 2019
 * @version: V1.0
 * @modified: yangkai.shen
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.mountain.sharding.jdbc.mapper")
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        log.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");
    }

}

