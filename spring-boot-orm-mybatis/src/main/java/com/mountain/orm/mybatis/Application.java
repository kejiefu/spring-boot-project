package com.mountain.orm.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.project.mountain.orm.mybatis.mapper")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        logger.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");

    }

}



