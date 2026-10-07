package in.dhruv.shoppingcart.service.kafka;

import in.dhruv.shoppingcart.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(
            topics = "order-created",
            groupId = "shopping-cart-order-group"
    )
    public void consume(OrderCreatedEvent event) {
        logger.info(
                "Order created event received: OrderId={}, userId={}, totalAmount={}",
                event.orderId(),
                event.userId(),
                event.totalAmount()
        );
    }
}
