package com.mountain.dynamic;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 官网示例 https://github.com/dynamic-datasource/dynamic-datasource-samples
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.mountain.dynamic.mapper")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        logger.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");

    }

}



