package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.OrderDTO;
import in.dhruv.shoppingcart.dto.OrderItemDTO;
import in.dhruv.shoppingcart.entity.Order;
import in.dhruv.shoppingcart.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        List<OrderItemDTO> orderItems = order
                .getOrderItems()
                .stream()
                .map(this::toOrderItemDTO)
                .toList();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderItems(orderItems);
        return orderDTO;
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        BigDecimal subtotal = orderItem
                .getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setSubtotal(subtotal);
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setProductName(orderItem.getProduct().getName());
        return orderItemDTO;
    }

}
