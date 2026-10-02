package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.order.OrderResponseDTO;
import in.dhruv.shoppingcart.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO checkout(
            Long userId
    );
    OrderResponseDTO getOrderById(
            Long orderId
    );
    List<OrderResponseDTO> getOrdersByUser(
            Long userId
    );
    OrderResponseDTO updateOrderStatus(
            Long orderId,
            OrderStatus status
    );
    OrderResponseDTO cancelOrder(
            Long orderId
    );
}
