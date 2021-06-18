package com.mountain.sharding.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


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

