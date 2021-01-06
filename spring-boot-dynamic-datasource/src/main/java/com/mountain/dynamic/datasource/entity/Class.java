package com.mountain.dynamic.datasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description TODO
 * @Date 2020/11/23 16:21
 * @Created by kejiefu
 */
@Data
@TableName("t_class")
public class Class extends BaseEntity {

    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 用户名
     */
    private String name;



}
