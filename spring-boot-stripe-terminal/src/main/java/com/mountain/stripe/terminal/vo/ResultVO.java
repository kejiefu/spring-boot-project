package com.mountain.stripe.terminal.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.StripeObject;
import lombok.Data;

import java.util.Map;

/**
 * 统一返回结果
 *
 * @author kejiefu
 * @date 2026-03-30
 */
@Data
public class ResultVO<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Integer code;

    private String message;

    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(normalizeStripeData(data));
        return result;
    }

    public static <T> ResultVO<T> error(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> T normalizeStripeData(T data) {
        if (data instanceof StripeObject) {
            try {
                Map<String, Object> map = OBJECT_MAPPER.readValue(
                        ((StripeObject) data).toJson(),
                        new TypeReference<Map<String, Object>>() {}
                );
                return (T) map;
            } catch (Exception ex) {
                return data;
            }
        }
        return data;
    }

}
