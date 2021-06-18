package com.mountain.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 4.0版本参考：
 * 5.0版本参考：https://gitee.com/edsword/datasource-demo
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.mountain.dynamic.mapper")
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        log.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");
    }

}



