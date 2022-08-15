package com.mountain.affirm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * https://docs.affirm.com/developers/reference/list_transactions
 */
@Service
@Slf4j
public class AffirmService {

    public String checkPay(String checkoutToken) {
        log.info("checkPay:{}", checkoutToken);
        return "";
    }

}
