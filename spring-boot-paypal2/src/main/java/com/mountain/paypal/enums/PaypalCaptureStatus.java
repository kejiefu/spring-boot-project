package com.mountain.paypal.enums;

/**
 * @author: kejiefu
 * @create: 2022-03-29 18:19
 **/
public enum PaypalCaptureStatus {

    /**
     * The funds for this captured payment were credited to the payee's PayPal account.
     */
    COMPLETED,
    /**
     * The funds could not be captured.
     */
    DECLINED,
    /**
     * An amount less than this captured payment's amount was partially refunded to the payer.
     */
    PARTIALLY_REFUNDED,
    /**
     * The funds for this captured payment was not yet credited to the payee's PayPal account. For more information, see
     * status.details.
     */
    PENDING,
    /**
     * An amount greater than or equal to this captured payment's amount was refunded to the payer.
     */
    REFUNDED,
    /**
     * There was an error while capturing payment.
     */
    FAILED,

}
