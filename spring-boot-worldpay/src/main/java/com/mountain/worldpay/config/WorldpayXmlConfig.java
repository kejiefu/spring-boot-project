package com.mountain.worldpay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: kejiefu
 * @create: 2022-03-29 10:28
 **/
@Component
@Slf4j
public class WorldpayXmlConfig {

    @javax.annotation.Resource
    ResourceLoader resourceLoader;

    @Value("${worldpay.installation.id}")
    private String installationId;

    @Value("${worldpay.merchant.code}")
    private String merchantCode;

    public static String WORLDPAY_SUBMIT = "";

    public static String WORLDPAY_INQUIRY = "";

    @PostConstruct
    public void init() throws IOException {
        initSubmit();
        initInquiry();
    }

    private void initSubmit() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:xml/worldpay-submit.xml");
        WORLDPAY_SUBMIT = getData(resource);
        log.info("WORLDPAY_SUBMIT:{}", WORLDPAY_SUBMIT);
    }

    private void initInquiry() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:xml/worldpay-inquiry.xml");
        WORLDPAY_INQUIRY = getData(resource);
        log.info("WORLDPAY_INQUIRY:{}", WORLDPAY_INQUIRY);
    }

    private String getData(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();
        String sTempOneLine;
        while ((sTempOneLine = bufferedReader.readLine()) != null) {
            stringBuffer.append(sTempOneLine);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return stringBuffer.toString().replace("merchantCodeParam", merchantCode).replace("installationIdParam", installationId);
    }

}
