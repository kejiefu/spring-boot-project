package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 创建支付请求参数
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class CreatePaymentVO {

    /**
     * 支付金额, 单位为分(cents)
     */
    private Long amount;

    /**
     * 货币类型, 默认usd
     */
    private String currency;

    /**
     * 业务系统订单描述
     */
    private String description;

    /**
     * 业务系统唯一标识(订单号)
     */
    private String referenceId;

    /**
     * 读卡器ID
     */
    private String readerId;

    /**
     * 捕获方式: automatic / manual
     */
    private String captureMethod;

}
