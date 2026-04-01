package com.mountain.stripe.terminal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 页面控制器, 提供POS前端页面路由
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Controller
@RequestMapping("/")
public class PageController {

    /**
     * POS收银台首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * SDK驱动模式示例页面
     */
    @RequestMapping(value = "/sdk", method = RequestMethod.GET)
    public String sdk() {
        return "sdk";
    }

}
