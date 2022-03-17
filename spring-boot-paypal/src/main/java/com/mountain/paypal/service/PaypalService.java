package com.mountain.paypal.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.mountain.paypal.enums.PaypalPaymentIntentEnum;
import com.mountain.paypal.enums.PaypalPaymentMethodEnum;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    @Value("${paypal.client.token.url}")
    private String clientTokenUrl;

    @Value("${paypal.client.payment.url}")
    private String clientPaymentUrl;

    @Value("${paypal.client.app}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    public Payment createPayment(
            Double total,
            String currency,
            PaypalPaymentMethodEnum method,
            PaypalPaymentIntentEnum intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        //远程调用paypal返回调用地址
        Payment payment1 = payment.create(apiContext);
        log.info("createPayment.payment:{}", payment1);
        return payment1;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        //远程调用paypal校验参数
        Payment payment1 = payment.execute(apiContext, paymentExecute);
        log.info("executePayment.payment:{}", payment1);
        return payment1;
    }


    public String check(String paymentId) {
        String url = clientTokenUrl;

        //获取accessToken
        Map<String, Object> fromMap = new HashMap<>(1);
        fromMap.put("grant_type", "client_credentials");
        String response = HttpRequest.post(url)
                .form(fromMap)
                .basicAuth(clientId, clientSecret)
                .execute().body();

        log.info("check.response:{},", response);

        String accessToken = JSONObject.parseObject(response)
                .getString("access_token");

        String tokenType = JSONObject.parseObject(response)
                .getString("token_type");

        String authorization = tokenType + " " + accessToken;

        String url2 = clientPaymentUrl + paymentId;
        String res = HttpRequest.get(url2)
                .header("Content-Type", "application/json")
                .header("Authorization", authorization)
                .execute().body();

        JSONObject response2 = JSONObject.parseObject(res);
        log.info("check.response2:{},", response2);
        String state = response2.getString("state");
        if ("approved".equalsIgnoreCase(state)) {
            //可以比较参数是否一致
            return "success";
        }
        return "fail";
    }

}
