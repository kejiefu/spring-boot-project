package com.mountain.paypal.service;

import cn.hutool.json.JSONUtil;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.http.serializer.Json;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaypalService {

    @Resource
    PayPalHttpClient payPalHttpClient;

    /**
     * 该标签将覆盖PayPal网站上PayPal帐户中的公司名称
     */
    public static final String BRAND_NAME = "mountain";
    /**
     * LOGIN。当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * BILLING。当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * NO_PREFERENCE。当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    public static final String LANDING_PAGE = "NO_PREFERENCE";
    /**
     * CONTINUE。将客户重定向到PayPal付款页面后，将出现“ 继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW。将客户重定向到PayPal付款页面后，出现“ 立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    public static final String USER_ACTION = "CONTINUE";
    /**
     * GET_FROM_FILE。使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING。从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS。使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    public static final String SHIPPING_PREFERENCE = "GET_FROM_FILE";


    public String createPayment(
            Double total,
            String currency,
            String description,
            String cancelUrl,
            String successUrl) {
        Order order = null;
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest();
        purchaseUnitRequest.amountWithBreakdown(new AmountWithBreakdown().currencyCode(currency).value(total.toString()));
        purchaseUnits.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName(BRAND_NAME)
                .landingPage(LANDING_PAGE)
                .cancelUrl(cancelUrl)
                .returnUrl(successUrl)
                .userAction(USER_ACTION)
                .shippingPreference(SHIPPING_PREFERENCE);
        orderRequest.applicationContext(applicationContext);


        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = payPalHttpClient.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            log.info("Order:{}", JSONUtil.toJsonStr(order));
            log.info("Order ID: " + order.id());

            order.links().forEach(link -> log.info(link.rel() + " => " + link.method() + ":" + link.href()));

            String redirectUrl = "";
            for (LinkDescription links : order.links()) {
                if ("approve".equals(links.rel())) {
                    redirectUrl = "redirect:" + links.href();
                    log.info("redirectUrl:{}", redirectUrl);
                }
            }
            return redirectUrl;
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                log.error(he.getMessage());
                he.headers().forEach(x -> log.info(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
            }
        }
        return null;
    }


    /**
     * OrdersGet（查询订单详情）
     *
     * @param paymentId
     * @return
     */
    public String checkPay(String paymentId) {
        try {
            OrdersGetRequest request = new OrdersGetRequest(paymentId);

            HttpResponse<Order> response = null;
            try {
                response = payPalHttpClient.execute(request);
            } catch (Exception ex) {
                log.error("payPalHttpClient.execute:", ex);
            }
            log.info("Status Code: " + response.statusCode());
            log.info("Status: " + response.result().status());
            log.info("Order id: " + response.result().id());
            if (response.result().purchaseUnits().get(0).payments() != null) {
                List<Capture> captures = response.result().purchaseUnits().get(0).payments().captures();
                if (captures != null) {
                    for (Capture capture : captures) {
                        //capture.id()  交易号
                        log.info("\t订单编号= " + capture.invoiceId() + "\tCapture Id= " + capture.id() + "\tCapture status= " + capture.status() + "\tCapture amount= " + capture.amount().currencyCode() + ":" + capture.amount().value());
                    }
                }
                List<Refund> refunds = response.result().purchaseUnits().get(0).payments().refunds();
                if (refunds != null) {
                    for (Refund refund : refunds) {
                        log.info("\t售后编号= " + refund.invoiceId() + "\tRefund Id= " + refund.id() + "\tRefund status= " + refund.status() + "\tRefund amount= " + refund.amount().currencyCode() + ":" + refund.amount().value());
                    }
                }

            }
            log.info("Links: ");
            for (com.paypal.orders.LinkDescription link : response.result().links()) {
                log.info("\t" + link.rel() + ": " + link.href() + "\tCall Type: " + link.method());
            }
            return "success";
        } catch (Exception ex) {
            log.error("checkPay:" + paymentId, ex);
        }
        return "fail";
    }


    /**
     * 用户通过CreateOrder生成 approveUrl 跳转paypal支付成功后，只是授权，并没有将用户的钱打入我们的paypal账户，我们需要通过 CaptureOrder接口，将钱打入我的PayPal账户
     *
     * @param paymentId
     * @return
     */
    public String capturePay(String paymentId)  {
        OrdersCaptureRequest request = new OrdersCaptureRequest(paymentId);
        request.requestBody(new OrderRequest());
        HttpResponse<Order> response = null;
        try {
            response = payPalHttpClient.execute(request);
            Payer buyer = response.result().payer();
            log.info("Buyer Email Address: {}", buyer.email());
            log.info("Buyer Name: {} {}", buyer.name().givenName(), buyer.name().surname());
            String json = new JSONObject(new Json().serialize(response.result())).toString(4);
            log.info("captureOrder response body: {}", json);
            log.info("Status Code = {}, Status = {}, OrderID = {}", response.statusCode(), response.result().status(), response.result().id());
            for (LinkDescription link : response.result().links()) {
                log.info("Links-{}: {}    \tCall Type: {}", link.rel(), link.href(), link.method());
            }
            for (PurchaseUnit purchaseUnit : response.result().purchaseUnits()) {
                for (Capture capture : purchaseUnit.payments().captures()) {
                    log.info("Capture id: {}", capture.id());
                    log.info("status: {}", capture.status());
                    log.info("invoice_id: {}", capture.invoiceId());
                    if ("COMPLETED".equals(capture.status())) {
                        //进行数据库操作，修改订单状态为已支付成功，尽快发货（配合回调和CapturesGet查询确定成功）
                        log.info("支付成功,状态为=COMPLETED");
                    }
                    if ("PENDING".equals(capture.status())) {
                        log.info("status_details: {}", capture.captureStatusDetails().reason());
                        String reason = "PENDING";
                        if (capture.captureStatusDetails() != null && capture.captureStatusDetails().reason() != null) {
                            reason = capture.captureStatusDetails().reason();
                        }
                        //进行数据库操作，修改订单状态为已支付成功，但触发了人工审核，请审核通过后再发货（配合回调和CapturesGet查询确定成功）
                        log.info("支付成功,状态为=PENDING : {}", reason);
                    }
                }
            }

        } catch (Exception e1) {
            log.error("调用paypal扣款失败:", e1);
        }
        return "success";
    }

}
