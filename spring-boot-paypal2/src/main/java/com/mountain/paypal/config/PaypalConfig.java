package com.mountain.paypal.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author kejiefu
 */
@Configuration
public class PaypalConfig {

    @Value("${paypal.client.app}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;


    @Bean
    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment payPalEnvironment;
        if ("sandbox".equals(mode)) {
            payPalEnvironment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        } else {
            payPalEnvironment = new PayPalEnvironment.Live(clientId, clientSecret);
        }
        return new PayPalHttpClient(payPalEnvironment);
    }


}
