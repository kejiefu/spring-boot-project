package com.mountain.stripe.terminal.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.File;
import com.stripe.model.StripeObject;
import com.stripe.model.terminal.Reader;
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
        if (stripeObject instanceof Reader) {
            logCollectInputsSignatureFiles((Reader) stripeObject);
        }
    }

    /**
     * collect_inputs 成功且含 signature 时，用 Stripe File API 拉取文件信息并打印到日志。
     */
    private void logCollectInputsSignatureFiles(Reader reader) {
        JSONObject root = JSONUtil.parseObj(reader.toJson());
        JSONObject action = root.getJSONObject("action");
        if (action == null || !"collect_inputs".equals(action.getStr("type"))) {
            return;
        }
        JSONObject collectInputs = action.getJSONObject("collect_inputs");
        if (collectInputs == null) {
            return;
        }
        JSONArray inputs = collectInputs.getJSONArray("inputs");
        if (inputs == null) {
            return;
        }
        for (int i = 0; i < inputs.size(); i++) {
            JSONObject in = inputs.getJSONObject(i);
            if (in == null || !"signature".equals(in.getStr("type"))) {
                continue;
            }
            JSONObject sig = in.getJSONObject("signature");
            if (sig == null) {
                continue;
            }
            String fileId = sig.getStr("value");
            if (StrUtil.isBlank(fileId)) {
                continue;
            }
            try {
                File file = File.retrieve(fileId);
                String line = String.format(
                        "[collect_inputs signature file] id=%s purpose=%s filename=%s size=%s type=%s expiresAt=%s url=%s",
                        file.getId(),
                        file.getPurpose(),
                        file.getFilename(),
                        file.getSize(),
                        file.getType(),
                        file.getExpiresAt(),
                        file.getUrl());
                log.info(line);
                System.out.println(line);
            } catch (StripeException e) {
                log.warn("拉取签名文件失败 fileId={}: {}", fileId, e.getMessage());
            }
        }
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
