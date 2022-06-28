package com.mountain.affirm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kejiefu
 * @Classname Application
 * @Description Application
 * @Date 2020/9/19 11:18
 */
@SpringBootApplication
public class AffirmApplication {

    private static final Logger logger = LoggerFactory.getLogger(AffirmApplication.class);

    public static void main(String[] args) {
        logger.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(AffirmApplication.class, args);
        logger.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");
    }

}