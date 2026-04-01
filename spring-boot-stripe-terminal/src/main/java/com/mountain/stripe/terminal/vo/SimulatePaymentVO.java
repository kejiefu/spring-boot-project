package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 模拟支付刷卡请求参数(测试用)
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class SimulatePaymentVO {

    /**
     * 读卡器ID
     */
    private String readerId;

}
