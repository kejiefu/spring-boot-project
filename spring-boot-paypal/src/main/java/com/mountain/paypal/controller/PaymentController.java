package com.mountain.paypal.controller;

import com.mountain.paypal.enums.PaypalPaymentIntentEnum;
import com.mountain.paypal.enums.PaypalPaymentMethodEnum;
import com.mountain.paypal.service.PaypalService;
import com.mountain.paypal.utils.URLUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kejiefu
 */
@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) {
        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
        try {
            Payment payment = paypalService.createPayment(
                    120.00,
                    "USD",
                    PaypalPaymentMethodEnum.PAYPAL,
                    PaypalPaymentIntentEnum.SALE,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    String redirectUrl = "redirect:" + links.getHref();
                    log.info("redirectUrl:{}", redirectUrl);
                    return redirectUrl;
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            log.info("校验交易,paymentId:{},payerId:{}", paymentId, payerId);
            Payment payment = paypalService.executePayment(paymentId, payerId);
            log.info("payment.getState:{}", payment.getState());
            if ("approved".equals(payment.getState())) {
                return "success";
            } else {
                log.error("交易失败");
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

}
