package com.mountain.stripe.controller;

import com.mountain.stripe.service.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kejiefu
 */
@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_CHECK_URL = "pay/check";

    public static final String PAYPAL_NOTIFY_URL = "pay/notify";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private StripeService stripeService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) {

        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay() {

        return "redirect:/";
    }

    /**
     * 手动拉取订单校验
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CHECK_URL)
    public String checkPay() {
        return "";
    }

    @RequestMapping(value = PAYPAL_NOTIFY_URL)
    public void notifyPay(HttpServletRequest request) {

    }

}
