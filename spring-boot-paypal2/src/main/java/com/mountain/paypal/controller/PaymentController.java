package com.mountain.paypal.controller;

import com.mountain.paypal.service.PaypalService;
import com.mountain.paypal.utils.UrlUtils;
import com.paypal.http.exceptions.SerializeException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_CHECK_URL = "pay/check";

    public static final String PAYPAL_NOTIFY_URL = "pay/notify";

    public static final String PAYPAL_CAPTURE_URL = "pay/capture";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) {
        String cancelUrl = UrlUtils.getBaseUrl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = UrlUtils.getBaseUrl(request) + "/" + PAYPAL_SUCCESS_URL;
        try {
            return paypalService.createPayment(
                    110.00,
                    "USD",
                    "payment description",
                    cancelUrl,
                    successUrl);
        } catch (Exception ex) {
            log.error("paypalService.createPayment", ex);
        }
        return "redirect:/";
    }


    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("token") String token, @RequestParam("PayerID") String payerId) {
        try {
            log.info("校验交易,token:{},payerId:{}", token, payerId);
            paypalService.checkPay(token);
            return "success";
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "redirect:/";
    }


    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CHECK_URL)
    public String checkPay(@RequestParam("paymentId") String paymentId) {
        return paypalService.checkPay(paymentId);
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CAPTURE_URL)
    public String capturePay(@RequestParam("paymentId") String paymentId){
        return paypalService.capturePay(paymentId);
    }

    @RequestMapping(value = PAYPAL_NOTIFY_URL)
    public void notifyPay(HttpServletRequest request) {

    }

}
