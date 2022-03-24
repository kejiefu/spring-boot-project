package com.mountain.worldpay.service;

import com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode;
import com.worldpay.gateway.clearwater.client.core.dto.common.CommonToken;
import com.worldpay.gateway.clearwater.client.core.dto.common.MerchantUrlConfig;
import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.WorldpayRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: kejiefu
 **/
@Slf4j
@Service
public class WorldpayService {

    @Value("${worldpay.client.key}")
    private String clientKey;

    @Value("${worldpay.service.key")
    private String serviceKey;

    public void createPayment() {
        WorldpayRestClient restClient = new WorldpayRestClient(serviceKey);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setToken("valid-token");
        orderRequest.setAmount(123);
        orderRequest.setCurrencyCode(CurrencyCode.USD);
        orderRequest.setName("test name");
        orderRequest.setOrderDescription("test description");

        MerchantUrlConfig merchantUrlConfig = new MerchantUrlConfig();
        merchantUrlConfig.setCancelUrl("");
        merchantUrlConfig.setSuccessUrl("");
        merchantUrlConfig.setFailureUrl("");
        merchantUrlConfig.setPendingUrl("");
        merchantUrlConfig.setErrorUrl("");
        orderRequest.setMerchantUrlConfig(merchantUrlConfig);

        try {
            OrderResponse orderResponse = restClient.getOrderService().create(orderRequest);
            System.out.println("Order code: " + orderResponse.getOrderCode());
        } catch (WorldpayException e) {
            System.out.println("Error code: " + e.getApiError().getCustomCode());
            System.out.println("Error description: " + e.getApiError().getDescription());
            System.out.println("Error message: " + e.getApiError().getMessage());
        }

    }

    public void createToken() {
        String baseUrl = "https://try.access.worldpay.com";
        WorldpayRestClient restClient = new WorldpayRestClient(baseUrl, serviceKey);
        //WorldpayRestClient restClient = new WorldpayRestClient(serviceKey);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey(clientKey);
        CommonToken commonToken = new CommonToken();

        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber("4242424242424242");
        cardRequest.setCvc("737");
        cardRequest.setName("1");
        cardRequest.setExpiryMonth(3);
        cardRequest.setExpiryYear(2030);

        //cardRequest.setIssueNumber(2);

        commonToken.setPaymentMethod(cardRequest);
        commonToken.setReusable(true);

        tokenRequest.setCommonToken(commonToken);

        try {
            TokenResponse tokenResponse = restClient.getTokenService().create(tokenRequest);
            log.info("token: " + tokenResponse.getToken());
        } catch (WorldpayException e) {
            log.error("WorldpayException:", e);
            log.error("Error code: " + e.getApiError().getCustomCode());
            log.error("Error description: " + e.getApiError().getDescription());
            log.error("Error message: " + e.getApiError().getMessage());
        }

    }

}
