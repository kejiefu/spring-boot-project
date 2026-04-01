package com.mountain.stripe.terminal.vo;

import lombok.Data;

/**
 * 取消读卡器操作请求参数
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class CancelActionVO {

    /**
     * 读卡器ID
     */
    private String readerId;

}
