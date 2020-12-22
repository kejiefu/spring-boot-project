package com.mountain.orm.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2020/11/23 16:21
 * @Created by kejiefu
 */
@Data
@TableName("t_class")
public class Class implements Serializable {

    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 上次更新时间
     */
    private Long updateTime;

}
