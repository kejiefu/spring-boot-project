package rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rabbitmq.config.RabbitMQConfig;

import javax.annotation.Resource;

import static rabbitmq.config.RabbitMQConfig.productEditQueueName;

/**
 * @author: kejiefu
 * @create: 2025-04-27 16:23
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TextController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public void send() {
        this.sendInventoryAddMessage();
        this.sendInventoryReduceMessage();
        this.sendProductEditMessage();
    }

    public void sendProductEditMessage() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                rabbitTemplate.convertAndSend(productEditQueueName, "edit message " );
            }).start();
        }
    }

    public void sendInventoryAddMessage() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                rabbitTemplate.convertAndSend(RabbitMQConfig.productInventoryAddQueueName, "add message ");
            }).start();
        }
    }

    public void sendInventoryReduceMessage() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                rabbitTemplate.convertAndSend(RabbitMQConfig.productInventoryReduceQueueName, "reduce message " );
            }).start();
        }
    }

}
