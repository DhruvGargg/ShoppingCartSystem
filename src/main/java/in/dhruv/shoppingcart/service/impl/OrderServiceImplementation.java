package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.OrderDTO;
import in.dhruv.shoppingcart.entity.*;
import in.dhruv.shoppingcart.enums.OrderStatus;
import in.dhruv.shoppingcart.mapper.OrderMapper;
import in.dhruv.shoppingcart.repository.CartItemRepository;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.OrderRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import in.dhruv.shoppingcart.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    OrderServiceImplementation(OrderRepository orderRepository,
                               UserRepository userRepository,
                               CartRepository cartRepository,
                               OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        List<Order> orderList = orderRepository
                .findByUserId(userId);
        return orderList.stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }
        if(order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order cannot be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        return orderMapper.toDTO(cancelledOrder);
    }

    @Override
    @Transactional
    public OrderDTO checkout(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            order.getOrderItems().add(orderItem);
            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return orderMapper.toDTO(savedOrder);
    }
}
