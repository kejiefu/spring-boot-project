package rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String productEditQueueName = "project.product.edit.queue";

    public static final String productInventoryAddQueueName = "project.product.inventory.add.queue";

    public static final String productInventoryReduceQueueName = "project.product.inventory.reduce.queue";

    @Bean
    public Queue productEditQueue() {
        return new Queue(productEditQueueName, true);
    }

    @Bean
    public Queue productInventoryAddQueue() {
        return new Queue(productInventoryAddQueueName, true);
    }

    @Bean
    public Queue productInventoryReduceQueue() {
        return new Queue(productInventoryReduceQueueName, true);
    }

}