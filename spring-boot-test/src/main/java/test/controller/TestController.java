package test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.config.NeedCoupon;
import test.service.business.ABusinessService;
import test.service.business.BBusinessService;

import javax.annotation.Resource;

/**
 * @author: kejiefu
 * @create: 2025-03-03 17:24
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ABusinessService aBusinessService;

    @Resource
    private BBusinessService bBusinessService;

    @GetMapping("/getTest1")
    @NeedCoupon
    public void getTest1() {
        aBusinessService.doSomething();
    }

    @GetMapping("/getTest2")
    @NeedCoupon
    public void getTest2() {
        bBusinessService.doSomething();
    }

    @GetMapping("/getTest3")
    public TestDataDTO getTest3() {
        TestDataDTO dataDTO = new TestDataDTO();
        dataDTO.setId(1899724075169869827L);
        return dataDTO;
    }


}
