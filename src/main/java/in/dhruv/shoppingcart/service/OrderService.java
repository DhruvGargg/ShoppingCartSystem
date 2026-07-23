package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.order.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder();
    List<OrderResponse> getOrders();
    OrderResponse getOrder(Long id);
}
