package test.service.business;

import org.springframework.stereotype.Service;
import test.config.RequestContext;

/**
 **/
@Service
public class BBusinessService {

    public void doSomething() {
        System.out.println("BBusinessService.doSomething：" + RequestContext.get("couponList"));
        int i = 10/0;
    }

}
