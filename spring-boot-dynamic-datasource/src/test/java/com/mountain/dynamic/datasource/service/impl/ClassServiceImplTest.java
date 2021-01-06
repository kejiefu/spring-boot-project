package com.mountain.dynamic.datasource.service.impl;

import com.mountain.dynamic.datasource.ApplicationTests;

import com.mountain.dynamic.datasource.entity.Class;
import com.mountain.dynamic.datasource.service.ClassService;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/1/6 15:36
 * @Created by kejiefu
 */
public class ClassServiceImplTest extends ApplicationTests {

    @Resource
    ClassService classService;

    @Test
    public void testSave() {
        for (int i = 1; i < 10; i++) {
            Class classEntity = new Class();
            classEntity.setId(RandomUtils.nextLong(1, 100000000000L));
            classService.saveOrUpdate(classEntity);
        }
    }

}