package com.mountain.paypal.service;

import cn.hutool.core.lang.UUID;
import com.mountain.paypal.enums.PaypalPaymentIntentEnum;
import com.mountain.paypal.enums.PaypalPaymentMethodEnum;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PaypalService {

    @Resource
    private APIContext apiContext;

    private static final String ORDER_STATE = "approved";

    private static final String PAYMENT_STATE = "completed";

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

        //内部的商户订单号
        String orderNo = UUID.fastUUID().toString();
        log.info("orderNo:{}", orderNo);
        transaction.setInvoiceNumber(orderNo);

        //发送支付通知的URL
        transaction.setNotifyUrl("/pay/notify");

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
        payment = payment.create(apiContext);
        log.info("createPayment.payment:{}", payment);
        return payment;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        //远程调用paypal校验参数
        payment = payment.execute(apiContext, paymentExecute);
        log.info("executePayment.payment:{}", payment);
        return payment;
    }

    public String checkPay(String paymentId, String orderNo) {
        try {
            Payment payment = Payment.get(apiContext, paymentId);
            log.info("check.payment:{}", payment);
            String state = payment.getState();
            if (ORDER_STATE.equalsIgnoreCase(state)) {
                List<Transaction> transactionList = payment.getTransactions();
                Transaction transaction = transactionList.get(0);
                String invoiceNumber = transaction.getInvoiceNumber();
                if (!Objects.equals(orderNo, invoiceNumber)) {
                    log.error("内部的商户订单号与paypal返回的商户订单号不同");
                } else {
                    List<RelatedResources> relatedResourcesList = transaction.getRelatedResources();
                    if (!CollectionUtils.isEmpty(relatedResourcesList)) {
                        RelatedResources relatedResources = relatedResourcesList.get(0);
                        if (Objects.nonNull(relatedResources)) {
                            Sale sale = relatedResources.getSale();
                            if (Objects.nonNull(sale) && PAYMENT_STATE.equalsIgnoreCase(sale.getState())) {
                                return "success";
                            }
                        }
                    }
                }
                return "fail";
            }
        } catch (Exception ex) {
            log.error("check:" + paymentId, ex);
        }
        return "fail";
    }

}
