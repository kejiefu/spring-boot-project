package com.mountain.stripe.terminal.controller;

import com.mountain.stripe.terminal.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Stripe Webhook回调控制器
 * 接收Stripe推送的Terminal读卡器事件和支付事件
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private WebhookService webhookService;

    /**
     * Stripe Webhook回调入口
     * 必须使用原始请求体(String)进行签名验证, 不能使用JSON反序列化后的对象
     */
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            webhookService.handleWebhookEvent(payload, sigHeader);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("Webhook processing error:", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
