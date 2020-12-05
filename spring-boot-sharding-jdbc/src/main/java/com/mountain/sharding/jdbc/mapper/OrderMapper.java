package com.mountain.sharding.jdbc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mountain.sharding.jdbc.model.Order;
import org.springframework.stereotype.Component;

/**
 * 订单表 Mapper
 */
@Component
public interface OrderMapper extends BaseMapper<Order> {
}
