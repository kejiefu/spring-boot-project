package com.project.mountain.scaffold.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author kejiefu
 * @Classname
 * @Description
 * @Date 2020/9/19 11:18
 */
@RestController
@Api(tags = "TextController", description = "测试内容相关")
public class TextController {

    @RequestMapping("/text")
    public String text() {
        return "hello word " + LocalDateTime.now().toString();
    }

    @ApiOperation(value = "获取数据", httpMethod = "GET", notes = "获取数据,传什么出什么")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "姓名", required = true, paramType = "query", dataType = "String"),})
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String api(@RequestParam(defaultValue = "ke") String user) {
        return user;
    }
}
