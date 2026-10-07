package in.dhruv.shoppingcart.service.kafka;

import in.dhruv.shoppingcart.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        kafkaTemplate.send(
                TOPIC,
                orderCreatedEvent.orderId().toString(),
                orderCreatedEvent
        );
    }

}
