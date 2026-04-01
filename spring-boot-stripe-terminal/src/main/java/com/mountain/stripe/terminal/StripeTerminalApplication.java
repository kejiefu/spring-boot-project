package com.mountain.stripe.terminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Stripe Terminal S710 集成示例应用入口
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@SpringBootApplication
public class StripeTerminalApplication {

    private static final Logger logger = LoggerFactory.getLogger(StripeTerminalApplication.class);

    public static void main(String[] args) {
        logger.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(StripeTerminalApplication.class, args);
        logger.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");
    }

}
