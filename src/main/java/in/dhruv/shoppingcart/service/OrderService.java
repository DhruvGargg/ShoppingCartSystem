package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.order.OrderResponse;
import in.dhruv.shoppingcart.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId);
    Order getOrderById(Long orderId);
    List<Order> getOrdersByUserId(Long userId);
    Order cancelOrder(Long orderId);
}
