package test.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: kejiefu
 * @create: 2025-03-12 15:55
 **/
@Data
public class TestDataDTO implements Serializable {

    /**
     * 这个字段默认是Long类型，在序列化时，会丢失精度，所以需要使用@JsonSerialize(using = ToStringSerializer.class)注解
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

}
