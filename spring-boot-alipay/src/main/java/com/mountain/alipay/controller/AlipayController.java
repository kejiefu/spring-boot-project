package com.mountain.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.mountain.alipay.model.Response;
import com.mountain.alipay.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: kejiefu
 * @create: 2022-04-29 11:56
 **/
@Controller
@RequestMapping("/")
@Slf4j
public class AlipayController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";

    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    public static final String PAYPAL_CHECK_URL = "pay/check";

    public static final String PAYPAL_NOTIFY_URL = "pay/notify";

    @Resource
    AlipayService alipayService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public Response pay(HttpServletRequest request) throws AlipayApiException {
        return alipayService.pay();
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
    public String checkPay(@RequestParam("orderNo") String orderNo) throws AlipayApiException {
        return alipayService.payCheck(orderNo);
    }

    @RequestMapping(value = PAYPAL_NOTIFY_URL)
    public void notifyPay(HttpServletRequest request) {

    }

}
