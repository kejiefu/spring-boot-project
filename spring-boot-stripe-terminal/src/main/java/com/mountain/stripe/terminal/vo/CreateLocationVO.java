package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 创建Location请求参数
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class CreateLocationVO {

    /**
     * 位置显示名称
     */
    private String displayName;

    /**
     * 地址行1
     */
    private String addressLine1;

    /**
     * 城市
     */
    private String city;

    /**
     * 州/省
     */
    private String state;

    /**
     * 国家(两位国家代码, 如US)
     */
    private String country;

    /**
     * 邮政编码
     */
    private String postalCode;

}
