package com.mountain.stripe.terminal.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Stripe Webhook 处理服务
 * 负责验证webhook签名和分发事件处理
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Service
@Slf4j
public class WebhookService {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    /**
     * 处理Stripe Webhook事件
     *
     * @param payload   请求体原始内容
     * @param sigHeader Stripe-Signature请求头
     * @throws SignatureVerificationException 签名验证失败
     */
    public void handleWebhookEvent(String payload, String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        log.info("Webhook event received: type={}, id={}", event.getType(), event.getId());

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (deserializer.getObject().isPresent()) {
            stripeObject = deserializer.getObject().get();
        }

        switch (event.getType()) {
            case "terminal.reader.action_succeeded":
                handleReaderActionSucceeded(stripeObject);
                break;
            case "terminal.reader.action_failed":
                handleReaderActionFailed(stripeObject);
                break;
            case "terminal.reader.action_updated":
                handleReaderActionUpdated(stripeObject);
                break;
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(stripeObject);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed(stripeObject);
                break;
            case "payment_intent.amount_capturable_updated":
                handlePaymentIntentCapturable(stripeObject);
                break;
            default:
                log.info("Unhandled webhook event type: {}", event.getType());
                break;
        }
    }

    /**
     * 处理读卡器操作成功事件
     * 此时支付已经授权成功, 可以通知前端更新UI
     */
    private void handleReaderActionSucceeded(StripeObject stripeObject) {
        log.info("Reader action SUCCEEDED: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

    /**
     * 处理读卡器操作失败事件
     * 检查failure_code判断失败原因(卡被拒、超时、客户取消等)
     */
    private void handleReaderActionFailed(StripeObject stripeObject) {
        log.warn("Reader action FAILED: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

    /**
     * 处理读卡器操作更新事件
     * 主要用于collect_payment_method流程中, 当卡被读取后触发
     */
    private void handleReaderActionUpdated(StripeObject stripeObject) {
        log.info("Reader action UPDATED: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

    /**
     * 处理支付成功事件
     */
    private void handlePaymentIntentSucceeded(StripeObject stripeObject) {
        log.info("PaymentIntent SUCCEEDED: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

    /**
     * 处理支付失败事件
     */
    private void handlePaymentIntentFailed(StripeObject stripeObject) {
        log.warn("PaymentIntent FAILED: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

    /**
     * 处理支付可捕获事件(manual capture场景下, 授权成功后触发)
     */
    private void handlePaymentIntentCapturable(StripeObject stripeObject) {
        log.info("PaymentIntent amount capturable updated: {}", stripeObject != null ? stripeObject.toJson() : "null");
    }

}
