package com.mountain.affirm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author kejiefu
 */
@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay() {
        try {
            return "success";
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }


}
