package test.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import test.service.CouponService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ke
 */
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethodAnnotation(NeedCoupon.class) != null) {
                // 仅当接口有 @NeedCoupon 注解时调用 getCoupon()
                CouponService couponService = SpringUtil.getBean(CouponService.class);
                RequestContext.put("couponList", couponService.getCouponList());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("afterCompletion.start:" + RequestContext.get("couponList"));
        // 无论是否触发，统一清理上下文（可选）
        RequestContext.clear();
        System.out.println("afterCompletion");
        System.out.println("afterCompletion.end:" + RequestContext.get("couponList"));
    }
}