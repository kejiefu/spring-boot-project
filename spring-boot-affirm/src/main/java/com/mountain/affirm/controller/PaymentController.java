package com.mountain.affirm.controller;

import com.mountain.affirm.service.AffirmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


/**
 * @author kejiefu
 */
@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    public static final String PAYPAL_CHECK_URL = "pay/check";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private AffirmService affirmService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("checkout_token") String checkoutToken) {
        log.info("successPay...");
        try {
            affirmService.checkPay(checkoutToken);
            return "success";
        } catch (Exception ex) {
            log.error("successPay:", ex);
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        log.info("cancelPay...");
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CHECK_URL)
    public String checkPay(@RequestParam("checkout_token") String checkoutToken) {
        return affirmService.checkPay(checkoutToken);
    }


}
