package com.mountain.orm.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mountain.orm.mybatis.plus.entity.Class;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/22 11:44
 * @Created by kejiefu
 */
public interface ClassService extends IService<Class> {

    boolean insert();

}

