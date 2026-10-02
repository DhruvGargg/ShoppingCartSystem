package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.order.OrderRequestDTO;
import in.dhruv.shoppingcart.dto.order.OrderResponseDTO;
import in.dhruv.shoppingcart.dto.orderitem.OrderItemRequestDTO;
import in.dhruv.shoppingcart.dto.orderitem.OrderItemResponseDTO;
import in.dhruv.shoppingcart.entity.Order;
import in.dhruv.shoppingcart.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    private OrderItemMapper orderItemMapper;

    public OrderResponseDTO toDTO(
            Order order
    ) {
        List<OrderItemResponseDTO> orderItems = order
                .getOrderItems()
                .stream()
                .map(orderItemMapper::toDTO)
                .toList();
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setItems(orderItems);
        return orderResponseDTO;
    }

    public Order toEntity(
        OrderRequestDTO orderRequestDTO
    ) {
        Order order = new Order();

        return order;
    }

}
