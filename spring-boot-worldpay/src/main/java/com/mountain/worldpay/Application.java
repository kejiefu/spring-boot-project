package com.mountain.worldpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kejiefu
 * @Classname
 * @Description
 * @Date 2020/9/19 11:18
 */
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("~~~~~~~~~~~~~~~~ worldpay program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        log.info("~~~~~~~~~~~~~~~~ worldpay program execute successfully~~~~~~~~~~~~~~~~!");
    }

}