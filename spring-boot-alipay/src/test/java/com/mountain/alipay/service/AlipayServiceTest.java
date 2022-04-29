package com.mountain.alipay.service;

import com.alipay.api.AlipayApiException;
import com.mountain.alipay.ApplicationTests;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class AlipayServiceTest extends ApplicationTests {

    @Resource
    AlipayService alipayService;

    @Test
    public void pay() throws AlipayApiException {
        alipayService.pay();
    }
}