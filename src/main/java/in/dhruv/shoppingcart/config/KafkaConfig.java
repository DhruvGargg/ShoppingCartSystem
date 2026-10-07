package in.dhruv.shoppingcart.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public NewTopic orderCreatedTopic()
    {
        return new NewTopic(
                "order-created",
                1,
                (short) 1
        );
    }
}
