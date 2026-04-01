package com.mountain.stripe.terminal.controller;

import com.mountain.stripe.terminal.service.TerminalService;
import com.mountain.stripe.terminal.vo.*;
import com.stripe.model.PaymentIntent;
import com.stripe.model.terminal.Location;
import com.stripe.model.terminal.LocationCollection;
import com.stripe.model.terminal.Reader;
import com.stripe.model.terminal.ReaderCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Stripe Terminal REST API控制器
 * 提供S710读卡器Server-Driven集成所需的全部接口
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@RestController
@RequestMapping("/api/terminal")
public class TerminalController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private TerminalService terminalService;

    /**
     * 创建连接令牌(前端JS SDK使用)
     */
    @PostMapping("/connection-token")
    public ResultVO<Map<String, String>> createConnectionToken() {
        try {
            String secret = terminalService.createConnectionToken();
            Map<String, String> data = new HashMap<>(2);
            data.put("secret", secret);
            return ResultVO.success(data);
        } catch (Exception e) {
            log.error("createConnectionToken error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 获取 SDK discoverReaders 默认参数。
     */
    @GetMapping("/sdk/config")
    public ResultVO<Map<String, Object>> getSdkConfig() {
        try {
            return ResultVO.success(terminalService.getSdkDiscoverConfig());
        } catch (Exception e) {
            log.error("getSdkConfig error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    // ==================== Location管理 ====================

    /**
     * 创建Location
     */
    @PostMapping("/locations")
    public ResultVO<Location> createLocation(@RequestBody CreateLocationVO vo) {
        try {
            Location location = terminalService.createLocation(
                    vo.getDisplayName(), vo.getAddressLine1(), vo.getCity(),
                    vo.getState(), vo.getCountry(), vo.getPostalCode());
            return ResultVO.success(location);
        } catch (Exception e) {
            log.error("createLocation error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 获取Location列表
     */
    @GetMapping("/locations")
    public ResultVO<LocationCollection> listLocations() {
        try {
            LocationCollection locations = terminalService.listLocations();
            return ResultVO.success(locations);
        } catch (Exception e) {
            log.error("listLocations error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    // ==================== Reader管理 ====================

    /**
     * 注册读卡器
     */
    @PostMapping("/readers")
    public ResultVO<Reader> registerReader(@RequestBody RegisterReaderVO vo) {
        try {
            Reader reader = terminalService.registerReader(
                    vo.getRegistrationCode(), vo.getLabel(), vo.getLocationId());
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("registerReader error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 获取读卡器列表
     */
    @GetMapping("/readers")
    public ResultVO<ReaderCollection> listReaders() {
        try {
            ReaderCollection readers = terminalService.listReaders();
            return ResultVO.success(readers);
        } catch (Exception e) {
            log.error("listReaders error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 获取读卡器详情(含action状态)
     */
    @GetMapping("/readers/{readerId}")
    public ResultVO<Reader> getReader(@PathVariable String readerId) {
        try {
            Reader reader = terminalService.getReader(readerId);
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("getReader error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 创建模拟读卡器(测试用)
     */
    @PostMapping("/readers/simulated")
    public ResultVO<Reader> createSimulatedReader(@RequestParam(required = false) String locationId) {
        try {
            Reader reader = terminalService.createSimulatedReader(locationId);
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("createSimulatedReader error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    // ==================== 支付处理 ====================

    /**
     * 创建支付并在读卡器上处理
     * Server-Driven模式: 后端直接通过Stripe API指令S710读卡器收款
     */
    @PostMapping("/payments")
    public ResultVO<Reader> createPayment(@RequestBody CreatePaymentVO vo) {
        try {
            Reader reader = terminalService.createAndProcessPayment(
                    vo.getAmount(), vo.getCurrency(), vo.getDescription(),
                    vo.getReferenceId(), vo.getReaderId(), vo.getCaptureMethod());
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("createPayment error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 创建PaymentIntent（SDK驱动模式）
     * 前端Terminal SDK通过collect/process完成收款，不在后端直接process reader。
     */
    @PostMapping("/payments/intents")
    public ResultVO<PaymentIntent> createPaymentIntentForSdk(@RequestBody CreatePaymentVO vo) {
        try {
            PaymentIntent paymentIntent = terminalService.createPaymentIntentForSdk(
                    vo.getAmount(), vo.getCurrency(), vo.getDescription(),
                    vo.getReferenceId(), vo.getCaptureMethod());
            return ResultVO.success(paymentIntent);
        } catch (Exception e) {
            log.error("createPaymentIntentForSdk error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 捕获已授权的支付
     */
    @PostMapping("/payments/capture")
    public ResultVO<PaymentIntent> capturePayment(@RequestBody CapturePaymentVO vo) {
        try {
            PaymentIntent paymentIntent = terminalService.capturePayment(vo.getPaymentIntentId());
            return ResultVO.success(paymentIntent);
        } catch (Exception e) {
            log.error("capturePayment error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 获取PaymentIntent状态
     */
    @GetMapping("/payments/{paymentIntentId}")
    public ResultVO<PaymentIntent> getPaymentIntent(@PathVariable String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = terminalService.getPaymentIntent(paymentIntentId);
            return ResultVO.success(paymentIntent);
        } catch (Exception e) {
            log.error("getPaymentIntent error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 取消读卡器上的当前操作
     */
    @PostMapping("/readers/cancel-action")
    public ResultVO<Reader> cancelAction(@RequestBody CancelActionVO vo) {
        try {
            Reader reader = terminalService.cancelReaderAction(vo.getReaderId());
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("cancelAction error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 模拟刷卡(仅simulated reader可用)
     */
    @PostMapping("/readers/simulate-payment")
    public ResultVO<Reader> simulatePayment(@RequestBody SimulatePaymentVO vo) {
        try {
            Reader reader = terminalService.simulatePresentPaymentMethod(vo.getReaderId());
            return ResultVO.success(reader);
        } catch (Exception e) {
            log.error("simulatePayment error:", e);
            return ResultVO.error(e.getMessage());
        }
    }

}
