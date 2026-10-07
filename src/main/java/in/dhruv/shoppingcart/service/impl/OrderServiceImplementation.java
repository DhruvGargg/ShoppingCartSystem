    package in.dhruv.shoppingcart.service.impl;

    import in.dhruv.shoppingcart.dto.order.OrderResponseDTO;
    import in.dhruv.shoppingcart.entity.*;
    import in.dhruv.shoppingcart.enums.OrderStatus;
    import in.dhruv.shoppingcart.event.OrderCreatedApplicationEvent;
    import in.dhruv.shoppingcart.event.OrderCreatedEvent;
    import in.dhruv.shoppingcart.exception.BadRequestException;
    import in.dhruv.shoppingcart.exception.ResourceNotFoundException;
    import in.dhruv.shoppingcart.mapper.OrderMapper;
    import in.dhruv.shoppingcart.repository.CartRepository;
    import in.dhruv.shoppingcart.repository.OrderRepository;
    import in.dhruv.shoppingcart.repository.UserRepository;
    import in.dhruv.shoppingcart.service.OrderService;
    import jakarta.transaction.Transactional;
    import org.springframework.context.ApplicationEventPublisher;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    public class OrderServiceImplementation implements OrderService {

        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final CartRepository cartRepository;
        private final OrderMapper orderMapper;

        private final ApplicationEventPublisher applicationEventPublisher;

        OrderServiceImplementation(OrderRepository orderRepository,
                                   UserRepository userRepository,
                                   CartRepository cartRepository,
                                   OrderMapper orderMapper,
                                   ApplicationEventPublisher applicationEventPublisher) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
            this.cartRepository = cartRepository;
            this.orderMapper = orderMapper;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Override
        public OrderResponseDTO getOrderById(
                Long orderId
        ) {
            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            return orderMapper.toDTO(order);
        }

        @Override
        public List<OrderResponseDTO> getOrdersByUser(
                Long userId
        ) {
            List<Order> orderList = orderRepository
                    .findByUserId(userId);
            return orderList.stream()
                    .map(orderMapper::toDTO)
                    .toList();
        }

        @Override
        public OrderResponseDTO updateOrderStatus(
                Long orderId,
                OrderStatus status
        ) {
            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            if (order.getStatus() == OrderStatus.CANCELLED) {
                throw new BadRequestException(
                        "Cancelled order cannot be updated"
                );
            }
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toDTO(updatedOrder);
        }

        @Override
        public OrderResponseDTO cancelOrder(Long orderId) {

            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Order not found"));
            if (order.getStatus() == OrderStatus.CANCELLED) {
                throw new BadRequestException(
                        "Order is already cancelled"
                );
            }
            if (order.getStatus() == OrderStatus.SHIPPED ||
                    order.getStatus() == OrderStatus.DELIVERED) {
                throw new BadRequestException(
                        "Order cannot be cancelled"
                );
            }
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            Order cancelledOrder = orderRepository.save(order);
            return orderMapper.toDTO(cancelledOrder);
        }

        @Override
        @Transactional
        public OrderResponseDTO checkout(
                Long userId
        ) {
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
            order.setUpdatedAt(LocalDateTime.now());

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
            applicationEventPublisher
                    .publishEvent(
                            new OrderCreatedApplicationEvent(
                                    new OrderCreatedEvent(
                                            savedOrder.getId(),
                                            userId,
                                            savedOrder.getTotalAmount(),
                                            savedOrder.getCreatedAt()
                                    )
                            )
                    );
            return orderMapper.toDTO(savedOrder);
        }
    }