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
@TableName("t_user")
public class User implements Serializable {

    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 上次更新时间
     */
    private Long updateTime;

}
