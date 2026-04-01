package com.mountain.stripe.terminal.config;

import com.stripe.Stripe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Stripe Terminal 配置类, 初始化Stripe API密钥
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Configuration
public class StripeTerminalConfig {

    private static final Logger log = LoggerFactory.getLogger(StripeTerminalConfig.class);

    @Value("${stripe.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
        log.info("Stripe API key initialized successfully");
    }

}
