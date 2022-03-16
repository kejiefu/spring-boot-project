package com.mountain.paypal.service;

import com.mountain.paypal.enums.PaypalPaymentIntentEnum;
import com.mountain.paypal.enums.PaypalPaymentMethodEnum;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(
            Double total, 
            String currency, 
            PaypalPaymentMethodEnum method,
            PaypalPaymentIntentEnum intent,
            String description, 
            String cancelUrl, 
            String successUrl) throws PayPalRESTException{
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
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        //远程调用paypal校验参数
        return payment.execute(apiContext, paymentExecute);
    }
}
