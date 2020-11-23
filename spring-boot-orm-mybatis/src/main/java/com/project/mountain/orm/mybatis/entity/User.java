package com.project.mountain.orm.mybatis.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2020/11/23 16:21
 * @Created by kejiefu
 */
@Data
@Table(name = "t_user")
public class User implements Serializable {

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
