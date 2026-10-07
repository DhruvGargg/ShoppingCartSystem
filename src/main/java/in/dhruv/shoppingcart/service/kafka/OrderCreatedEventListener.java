package in.dhruv.shoppingcart.service.kafka;

import in.dhruv.shoppingcart.event.OrderCreatedApplicationEvent;
import in.dhruv.shoppingcart.event.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderCreatedEventListener {

    private final OrderEventProducer orderEventProducer;

    public OrderCreatedEventListener(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedApplicationEvent event) {
        orderEventProducer.publishOrderCreated(
                event.orderCreatedEvent()
        );
    }
}
