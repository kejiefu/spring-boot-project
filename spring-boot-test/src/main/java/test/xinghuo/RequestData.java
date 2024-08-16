package test.xinghuo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: kejiefu
 * @create: 2024-08-16 16:51
 **/
@Data
public class RequestData implements Serializable {

    private String model;

    private List<Message> messages;

    private Boolean stream;

    @Data
    public static class Message implements Serializable {

        private String role;

        private String content;

    }

}
