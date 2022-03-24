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

/**
 * @author: kejiefu
 **/
public class WorldpayService {

    public void createPayment(){
        WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");

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

    public void createToken(){
        WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey("");
        CommonToken commonToken = new CommonToken();

        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber("4242424242424242");
        cardRequest.setCvc("737");
        cardRequest.setName("1");
        cardRequest.setExpiryMonth(30);
        cardRequest.setExpiryYear(3);

        commonToken.setPaymentMethod(cardRequest);

        tokenRequest.setCommonToken(commonToken);

        try {
            TokenResponse tokenResponse = restClient.getTokenService().create(tokenRequest);
            System.out.println("token: " + tokenResponse.getToken());
        } catch (WorldpayException e) {
            System.out.println("Error code: " + e.getApiError().getCustomCode());
            System.out.println("Error description: " + e.getApiError().getDescription());
            System.out.println("Error message: " + e.getApiError().getMessage());
        }

    }

}
