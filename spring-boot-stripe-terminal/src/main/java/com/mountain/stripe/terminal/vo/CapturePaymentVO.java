package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 捕获支付请求参数
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class CapturePaymentVO {

    /**
     * PaymentIntent ID
     */
    private String paymentIntentId;

}
