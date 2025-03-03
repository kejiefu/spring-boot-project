package test.service;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: kejiefu
 * @create: 2025-03-03 17:20
 **/
@Service
public class CouponService {

    public List<String> getCouponList() {
        List<String> list = Lists.list("A", "B", "C");
        return list;
    }

}
