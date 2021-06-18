package com.mountain.dynamic.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/11/23 16:21
 * @Created by kejiefu
 */
@Data
@TableName("t_teacher")
public class Teacher extends BaseEntity {

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
     * t_user.id
     */
    private Long userId;

}
