package in.dhruv.shoppingcart.service.kafka;

import in.dhruv.shoppingcart.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderProcessingService.class);

    public void processOrder(OrderCreatedEvent event) {
        logger.info(
                "Processing order: orderId={}, userId={}, totalAmount={}",
                event.orderId(),
                event.userId(),
                event.totalAmount()
        );
    }
}
