package rabbitmq.consumer;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static rabbitmq.config.RabbitMQConfig.productInventoryAddQueueName;
import static rabbitmq.config.RabbitMQConfig.productInventoryReduceQueueName;

@Component
public class ProductInventoryConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductInventoryConsumer.class);

    @RabbitListener(queues = {productInventoryAddQueueName, productInventoryReduceQueueName})
    public void consumeInventoryMessage(String message, Channel channel, Message amqpMessage) {
        try {
            logger.info("Consumed inventory message: {},{}", message, DateUtil.formatDateTime(new Date()));

            // 处理库存消息的逻辑
            // 假设这里完成了库存消息的处理
            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
            //不执行ack话，rabbitmq消息数据一直都会存在

            Thread.sleep(1000);

        } catch (IOException e) {
            logger.error("Failed to acknowledge inventory message: {}", message, e);
            try {
                // 处理失败，拒绝消息，可选择是否重新入队
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ex) {
                logger.error("Failed to nack inventory message: {}", message, ex);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

