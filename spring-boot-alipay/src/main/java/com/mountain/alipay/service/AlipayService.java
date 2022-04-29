package com.mountain.alipay.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.mountain.alipay.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: kejiefu
 * @create: 2022-04-28 18:26
 **/
@Service
@Slf4j
public class AlipayService {

    @Value("${appid}")
    private String appid;

    @Value("${privateKey}")
    private String privateKey;

    @Value("${alipayPublicKey}")
    private String alipayPublicKey;

    @Value("${serverUrl}")
    private String serverUrl;


    /**
     * @return
     * @throws AlipayApiException
     */
    public Response pay() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appid, privateKey, "json", "UTF-8", alipayPublicKey, "RSA2");
        Response response = new Response();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl("");
        request.setReturnUrl("");
        JSONObject bizContent = new JSONObject();
        String orderNo = UUID.randomUUID().toString();
        log.info("orderNo:{}", orderNo);
        bizContent.put("out_trade_no", orderNo);
        bizContent.put("total_amount", 12);
        bizContent.put("subject", "测试商品");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.pageExecute(request);
        log.info("alipayTradePagePayResponse:{}", JSONUtil.toJsonStr(alipayTradePagePayResponse));
        if (alipayTradePagePayResponse.isSuccess()) {
            log.info("调用成功");
            response.setData(alipayTradePagePayResponse.getBody());
            return response;
        } else {
            log.error("调用失败");
        }
        return null;
    }

    /**
     * https://opendocs.alipay.com/apis/028pxr
     * trade_status 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
     * @param orderNo orderNo
     * @return
     * @throws AlipayApiException
     */
    public String payCheck(String orderNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appid, privateKey, "json", "UTF-8", alipayPublicKey, "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(request);
        log.info("alipayTradeQueryResponse:{}", JSONUtil.toJsonStr(alipayTradeQueryResponse));
        if (alipayTradeQueryResponse.isSuccess()) {
            log.info("调用成功");
            return "success";
        } else {
            log.error("调用失败");
        }
        return "fail";
    }

}
