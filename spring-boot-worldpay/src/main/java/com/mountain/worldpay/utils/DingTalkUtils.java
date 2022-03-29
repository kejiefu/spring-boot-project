package com.mountain.worldpay.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kejiefu
 * @create 2020/8/19.
 */
@Slf4j
@Component
public class DingTalkUtils {

    private static final String URL = "https://oapi.dingtalk.com/robot/send";

    private static RestTemplate restTemplate;

    private static String secret;

    private static String accessToken;

    @Value(value = "${dingtalk.notification.secret}")
    private void setSecret(String secret) {
        DingTalkUtils.secret = secret;
    }

    @Value(value = "${dingtalk.notification.access.token}")
    private void setAccessToken(String accessToken) {
        DingTalkUtils.accessToken = accessToken;
    }

    @Autowired
    public DingTalkUtils(RestTemplate restTemplate) {
        DingTalkUtils.restTemplate = restTemplate;
    }

    public static String sign(long timestamp) {
        String stringToSign = timestamp + "\n" + secret;
        String sign = null;
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        } catch (Exception e) {
            log.info("获取钉钉机器人签名失败");
        }
        return sign;
    }

    public static void notification(String message) {
        long timestamp = System.currentTimeMillis();
        String sign = DingTalkUtils.sign(timestamp);
        String url = URL + "?access_token=" + accessToken + "&timestamp=" + timestamp + "&sign=" + sign;

        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        //body
        Map<String, String> content = new HashMap<>();
        content.put("content", message);
        JSONObject param = new JSONObject();
        param.put("msgtype", "text");
        param.put("text", JSON.toJSON(content));

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(param, requestHeaders);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (HttpStatus.OK.equals(httpStatus)) {
            log.info("成功发送钉钉通知");
        } else {
            log.error("发送钉钉通知失败");
        }
    }
}