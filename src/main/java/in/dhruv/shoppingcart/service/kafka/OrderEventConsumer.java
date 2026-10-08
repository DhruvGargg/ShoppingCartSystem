package in.dhruv.shoppingcart.service.kafka;

import in.dhruv.shoppingcart.event.OrderCreatedEvent;
import in.dhruv.shoppingcart.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final OrderProcessingService orderProcessingService;

    public OrderEventConsumer(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "shopping-cart-order-group"
    )
    public void consume(OrderCreatedEvent event) {
        orderProcessingService.processOrder(event);
    }
}
