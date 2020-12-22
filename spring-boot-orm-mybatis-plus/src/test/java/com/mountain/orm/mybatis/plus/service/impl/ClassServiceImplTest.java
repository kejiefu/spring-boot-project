package com.mountain.orm.mybatis.plus.service.impl;

import com.mountain.orm.mybatis.plus.ApplicationTests;
import com.mountain.orm.mybatis.plus.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/22 11:50
 * @Created by kejiefu
 */
@Slf4j
public class ClassServiceImplTest extends ApplicationTests {

    @Resource
    ClassService classService;

    @Test
    public void insert() {
        try {
            classService.insert();
        } catch (Exception ex) {
            log.error("classService.insert:", ex);
        }
    }
}