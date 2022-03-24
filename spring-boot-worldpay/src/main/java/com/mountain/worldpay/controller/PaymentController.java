package com.mountain.worldpay.controller;

import com.mountain.worldpay.service.WorldpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kejiefu
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_CHECK_URL = "pay/check";

    public static final String PAYPAL_NOTIFY_URL = "pay/notify";

    @Resource
    WorldpayService worldpayService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) {
        worldpayService.createToken();;
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {

        return "redirect:/";
    }

    /**
     * 手动拉取订单校验
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CHECK_URL)
    public String checkPay(@RequestParam("paymentId") String paymentId, @RequestParam("orderNo") String orderNo) {
        return "";
    }

    @RequestMapping(value = PAYPAL_NOTIFY_URL)
    public void notifyPay(HttpServletRequest request) {

    }

}
