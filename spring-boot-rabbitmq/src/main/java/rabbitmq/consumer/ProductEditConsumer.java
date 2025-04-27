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

import static rabbitmq.config.RabbitMQConfig.productEditQueueName;

@Component
public class ProductEditConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductEditConsumer.class);

    // 最大 3 个消费者
    @RabbitListener(queues = {productEditQueueName}, concurrency = "2")
    public void consumeProductEditMessage(String message, Channel channel, Message amqpMessage) {
        // 处理 product 编辑消息的逻辑
        try {
            logger.info("Consumed product edit message: {},{}", message, DateUtil.formatDateTime(new Date()));

            // 处理库存消息的逻辑
            // 假设这里完成了库存消息的处理
            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
            //不执行ack话，rabbitmq消息数据一直都会存在

            Thread.sleep(5000);

        } catch (IOException e) {
            logger.error("Failed to acknowledge product edit message: {}", message, e);
            try {
                // 处理失败，拒绝消息，可选择是否重新入队
                // 手动确认消息：调用 channel.basicAck 方法来手动确认消息。第一个参数 amqpMessage.getMessageProperties().getDeliveryTag() 是消息的唯一标识，第二个参数 false 表示只确认当前消息，不批量确认。
                // 异常处理：如果在处理消息或确认消息时发生 IOException，会记录错误日志，并调用 channel.basicNack 方法拒绝消息。basicNack 方法的第三个参数 false 表示不将消息重新入队。
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ex) {
                logger.error("Failed to nack product edit message: {}", message, ex);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}