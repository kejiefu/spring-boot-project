package test.service.business;

import org.springframework.stereotype.Service;
import test.config.RequestContext;

/**
 *
 **/
@Service
public class ABusinessService {

    public void doSomething() {
        System.out.println("ABusinessService.doSomething：" + RequestContext.get("couponList"));

    }

}
