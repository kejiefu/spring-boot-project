package com.mountain.stripe.terminal.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.terminal.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stripe Terminal 核心服务类
 * 提供连接令牌、Location管理、Reader管理、支付处理等功能
 * 适配 stripe-java 22.3.0 版本, 使用 Map 传参方式
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Service
@Slf4j
public class TerminalService {

    @Value("${stripe.terminal.location-id:}")
    private String defaultLocationId;

    @Value("${stripe.terminal.simulated:true}")
    private boolean simulated;

    /**
     * 创建连接令牌, 用于前端JS SDK初始化
     *
     * @return ConnectionToken的secret
     * @throws StripeException Stripe API异常
     */
    public String createConnectionToken() throws StripeException {
        Map<String, Object> params = new HashMap<>(2);
        ConnectionToken token = ConnectionToken.create(params);
        log.info("ConnectionToken created: {}", token.getSecret().substring(0, 20) + "...");
        return token.getSecret();
    }

    /**
     * 创建Terminal Location
     *
     * @param displayName 显示名称
     * @param line1       地址行1
     * @param city        城市
     * @param state       州
     * @param country     国家代码
     * @param postalCode  邮编
     * @return Location对象
     * @throws StripeException Stripe API异常
     */
    public Location createLocation(String displayName, String line1, String city,
                                   String state, String country, String postalCode) throws StripeException {
        Map<String, Object> address = new HashMap<>(8);
        address.put("line1", line1);
        address.put("city", city);
        address.put("state", state);
        address.put("country", country);
        address.put("postal_code", postalCode);

        Map<String, Object> params = new HashMap<>(4);
        params.put("display_name", displayName);
        params.put("address", address);

        Location location = Location.create(params);
        log.info("Location created: id={}, displayName={}", location.getId(), displayName);
        return location;
    }

    /**
     * 获取Location列表
     *
     * @return LocationCollection
     * @throws StripeException Stripe API异常
     */
    public LocationCollection listLocations() throws StripeException {
        Map<String, Object> params = new HashMap<>(2);
        params.put("limit", 100);
        LocationCollection locations = Location.list(params);
        log.info("Locations found: {}", locations.getData().size());
        return locations;
    }

    /**
     * 注册读卡器到Stripe Terminal
     *
     * @param registrationCode 设备配对码
     * @param label            读卡器标签
     * @param locationId       Location ID
     * @return Reader对象
     * @throws StripeException Stripe API异常
     */
    public Reader registerReader(String registrationCode, String label, String locationId) throws StripeException {
        String locId = StrUtil.isNotBlank(locationId) ? locationId : defaultLocationId;
        Map<String, Object> params = new HashMap<>(4);
        params.put("registration_code", registrationCode);
        params.put("label", label);
        params.put("location", locId);
        Reader reader = Reader.create(params);
        log.info("Reader registered: id={}, label={}, deviceType={}", reader.getId(), label, reader.getDeviceType());
        return reader;
    }

    /**
     * 获取读卡器列表
     *
     * @return ReaderCollection
     * @throws StripeException Stripe API异常
     */
    public ReaderCollection listReaders() throws StripeException {
        Map<String, Object> params = new HashMap<>(4);
        params.put("limit", 100);
        if (StrUtil.isNotBlank(defaultLocationId)) {
            params.put("location", defaultLocationId);
        }
        ReaderCollection readers = Reader.list(params);
        log.info("Readers found: {}", readers.getData().size());
        return readers;
    }

    /**
     * 获取读卡器详情
     *
     * @param readerId 读卡器ID
     * @return Reader对象
     * @throws StripeException Stripe API异常
     */
    public Reader getReader(String readerId) throws StripeException {
        Reader reader = Reader.retrieve(readerId);
        log.info("Reader retrieved: id={}, status={}", reader.getId(), reader.getStatus());
        return reader;
    }

    /**
     * 创建PaymentIntent并通过Server-Driven方式在读卡器上处理支付
     *
     * @param amount        金额(分)
     * @param currency      货币类型
     * @param description   描述
     * @param referenceId   业务唯一标识
     * @param readerId      读卡器ID
     * @param captureMethod 捕获方式
     * @return Reader对象(含action状态)
     * @throws StripeException Stripe API异常
     */
    public Reader createAndProcessPayment(Long amount, String currency, String description,
                                          String referenceId, String readerId,
                                          String captureMethod) throws StripeException {
        String cur = StrUtil.isNotBlank(currency) ? currency : "usd";
        String capMethod = StrUtil.isNotBlank(captureMethod) ? captureMethod : "manual";

        Map<String, Object> piParams = new HashMap<>(8);
        piParams.put("amount", amount);
        piParams.put("currency", cur);
        piParams.put("payment_method_types", Collections.singletonList("card_present"));
        piParams.put("capture_method", capMethod);

        if (StrUtil.isNotBlank(description)) {
            piParams.put("description", description);
        }

        if (StrUtil.isNotBlank(referenceId)) {
            Map<String, String> metadata = new HashMap<>(2);
            metadata.put("reference_id", referenceId);
            piParams.put("metadata", metadata);
        }

        PaymentIntent paymentIntent = PaymentIntent.create(piParams);
        log.info("PaymentIntent created: id={}, amount={}, currency={}", paymentIntent.getId(), amount, cur);

        Reader reader = Reader.retrieve(readerId);
        Map<String, Object> processParams = new HashMap<>(4);
        processParams.put("payment_intent", paymentIntent.getId());
        reader = reader.processPaymentIntent(processParams);
        log.info("Payment processing on reader: readerId={}, actionStatus={}",
                readerId, JSONUtil.toJsonStr(reader.getAction()));

        return reader;
    }

    /**
     * 手动捕获已授权的PaymentIntent
     *
     * @param paymentIntentId PaymentIntent ID
     * @return PaymentIntent对象
     * @throws StripeException Stripe API异常
     */
    public PaymentIntent capturePayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        paymentIntent = paymentIntent.capture();
        log.info("PaymentIntent captured: id={}, status={}", paymentIntent.getId(), paymentIntent.getStatus());
        return paymentIntent;
    }

    /**
     * 取消读卡器上的当前操作
     *
     * @param readerId 读卡器ID
     * @return Reader对象
     * @throws StripeException Stripe API异常
     */
    public Reader cancelReaderAction(String readerId) throws StripeException {
        Reader reader = Reader.retrieve(readerId);
        reader = reader.cancelAction();
        log.info("Reader action cancelled: readerId={}", readerId);
        return reader;
    }

    /**
     * 获取PaymentIntent详情
     *
     * @param paymentIntentId PaymentIntent ID
     * @return PaymentIntent对象
     * @throws StripeException Stripe API异常
     */
    public PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        log.info("PaymentIntent retrieved: id={}, status={}", paymentIntent.getId(), paymentIntent.getStatus());
        return paymentIntent;
    }

    /**
     * 模拟读卡器刷卡(仅在测试环境下使用simulated reader)
     *
     * @param readerId 读卡器ID
     * @return Reader对象
     * @throws StripeException Stripe API异常
     */
    public Reader simulatePresentPaymentMethod(String readerId) throws StripeException {
        Reader reader = Reader.retrieve(readerId);
        reader = reader.getTestHelpers().presentPaymentMethod();
        log.info("Simulated payment method presented on reader: readerId={}", readerId);
        return reader;
    }

    /**
     * 创建模拟读卡器(测试用)
     *
     * @param locationId Location ID
     * @return Reader对象
     * @throws StripeException Stripe API异常
     */
    public Reader createSimulatedReader(String locationId) throws StripeException {
        String locId = StrUtil.isNotBlank(locationId) ? locationId : defaultLocationId;
        Map<String, Object> params = new HashMap<>(4);
        params.put("registration_code", "simulated-wpe");
        params.put("label", "Simulated S710 Reader");
        params.put("location", locId);
        Reader reader = Reader.create(params);
        log.info("Simulated reader created: id={}, deviceType={}", reader.getId(), reader.getDeviceType());
        return reader;
    }

}
