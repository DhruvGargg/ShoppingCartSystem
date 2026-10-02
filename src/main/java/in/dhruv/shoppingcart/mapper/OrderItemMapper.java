package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.order.OrderRequestDTO;
import in.dhruv.shoppingcart.dto.orderitem.OrderItemResponseDTO;
import in.dhruv.shoppingcart.entity.Order;
import in.dhruv.shoppingcart.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemMapper {

    public OrderItemResponseDTO toDTO(OrderItem order)
    {
        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
        orderItemResponseDTO.setProductName(order.getProduct().getName());
        orderItemResponseDTO.setQuantity(order.getQuantity());
        orderItemResponseDTO.setProductId(order.getProduct().getId());
        return orderItemResponseDTO;
    }

    public OrderItem toEntity(
            OrderRequestDTO orderRequestDTO
    ) {
        OrderItem orderItem = new OrderItem();
        return orderItem;
    }
}
