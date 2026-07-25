package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.OrderDTO;
import in.dhruv.shoppingcart.dto.order.OrderResponse;
import in.dhruv.shoppingcart.entity.Order;
import in.dhruv.shoppingcart.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDTO checkout(Long userId);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getOrdersByUser(Long userId);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    OrderDTO cancelOrder(Long orderId);
}
