package com.mountain.stripe.service;

import cn.hutool.json.JSONUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * @author: kejiefu
 **/
@Service
@Slf4j
public class StripeService {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51KfypZDDEWbnrS7ImZMQikqdR8jF4bx8MQnNrtsXogSG4pt9WTRUdM0y8TsW7kRPjNf1CgzfQ6xVUiNC938yG04H00UHmdVaU5";
    }

    public String createPayment() {
        try {

            Map<String, Object> params = new HashMap<>(16);

            //预填充客户邮箱
            //params.put("customer_email", "123@163.com");

            ArrayList<String> paymentMethodTypes = new ArrayList<>(10);
            paymentMethodTypes.add("card");
            params.put("payment_method_types", paymentMethodTypes);

            ArrayList<HashMap<String, Object>> lineItems = new ArrayList<>();
            HashMap<String, Object> lineItem = new HashMap<>(16);
            lineItem.put("name", "KE");
            lineItem.put("description", "这是一个测试单描述");
            lineItem.put("amount", 500);
            lineItem.put("currency", "USD");
            lineItem.put("quantity", 1);
            lineItems.add(lineItem);
            params.put("line_items", lineItems);

            //必须使用https 返回的回调地址
            //业务系统唯一标识 即订单唯一编号
            params.put("client_reference_id", UUID.randomUUID().toString());
            params.put("success_url", "https://example.com/success");
            params.put("cancel_url", "https://example.com/cancel");
            log.info("params:{}", JSONUtil.toJsonStr(params));
            Session session = Session.create(params);
            log.info("session:{}", JSONUtil.toJsonStr(session));
            return session.getUrl();
        } catch (StripeException ex) {
            log.error("createPayment:", ex);
        }
        return null;
    }

    public String checkPay(String paymentId) {
        try {
            Map<String, Object> params = new HashMap<>(16);
            params.put("payment_intent", paymentId);
            SessionCollection sessionCollection = Session.list(params);
            log.info("sessionCollection:{}", JSONUtil.toJsonStr(sessionCollection));
            List<Session> sessionList = sessionCollection.getData();
            for (Session session : sessionList) {
                if ("complete".equals(session.getStatus()) && "paid".equals(session.getPaymentStatus())) {
                    return "success";
                }
            }
        } catch (Exception ex) {
            log.error("check:" + paymentId, ex);
        }
        return "fail";
    }

}
