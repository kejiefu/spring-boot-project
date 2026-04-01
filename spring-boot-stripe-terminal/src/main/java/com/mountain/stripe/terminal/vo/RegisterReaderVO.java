package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 注册读卡器请求参数
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class RegisterReaderVO {

    /**
     * 读卡器配对码(在S710设备屏幕上显示)
     */
    private String registrationCode;

    /**
     * 读卡器标签名称
     */
    private String label;

    /**
     * 所属Location ID
     */
    private String locationId;

}
