package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.order.OrderResponseDTO;
import in.dhruv.shoppingcart.entity.*;
import in.dhruv.shoppingcart.enums.OrderStatus;
import in.dhruv.shoppingcart.mapper.OrderMapper;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.OrderRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplementationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    void getOrderByIdShouldReturnOrderWhenOrderExists()
    {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderMapper.toDTO(order))
        .thenReturn(orderResponseDTO);

        OrderResponseDTO result =
                orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderResponseDTO, result);

        verify(orderRepository).findById(orderId);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderDoesNotExist() {

        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.getOrderById(orderId)
                );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void getOrdersByUser_ShouldReturnOrders_WhenOrdersExist() {

        Long userId = 1L;

        Order order1 = new Order();
        Order order2 = new Order();

        OrderResponseDTO dto1 = new OrderResponseDTO();
        OrderResponseDTO dto2 = new OrderResponseDTO();

        when(orderRepository.findByUserId(userId))
                .thenReturn(List.of(order1, order2));

        when(orderMapper.toDTO(order1))
                .thenReturn(dto1);

        when(orderMapper.toDTO(order2))
                .thenReturn(dto2);

        List<OrderResponseDTO> result =
                orderService.getOrdersByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(orderRepository).findByUserId(userId);
        verify(orderMapper).toDTO(order1);
        verify(orderMapper).toDTO(order2);
    }


    @Test
    void getOrdersByUser_ShouldReturnEmptyList_WhenNoOrdersExist() {

        Long userId = 1L;

        when(orderRepository.findByUserId(userId))
                .thenReturn(List.of());

        List<OrderResponseDTO> result =
                orderService.getOrdersByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository).findByUserId(userId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus_WhenOrderExists() {

        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CONFIRMED);

        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(order))
                .thenReturn(order);

        when(orderMapper.toDTO(order))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.updateOrderStatus(
                        orderId,
                        OrderStatus.SHIPPED
                );

        assertNotNull(result);
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        assertEquals(responseDTO, result);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void updateOrderStatus_ShouldThrowException_WhenOrderDoesNotExist() {

        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.updateOrderStatus(
                                orderId,
                                OrderStatus.SHIPPED
                        )
                );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }

    @Test
    void cancelOrder_ShouldCancelOrder_WhenOrderCanBeCancelled() {

        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CONFIRMED);

        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(order))
                .thenReturn(order);

        when(orderMapper.toDTO(order))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.cancelOrder(orderId);

        assertNotNull(result);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(responseDTO, result);

        verify(orderRepository).findById(orderId);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenAlreadyCancelled() {

        Long orderId = 1L;

        Order order = new Order();
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.cancelOrder(orderId)
                );

        assertEquals(
                "Order is already cancelled",
                exception.getMessage()
        );

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderIsShipped() {

        Long orderId = 1L;

        Order order = new Order();
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.cancelOrder(orderId)
                );

        assertEquals(
                "Order cannot be cancelled",
                exception.getMessage()
        );

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }


    @Test
    void cancelOrder_ShouldThrowException_WhenOrderIsDelivered() {

        Long orderId = 1L;

        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.cancelOrder(orderId)
                );

        assertEquals(
                "Order cannot be cancelled",
                exception.getMessage()
        );

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }


    @Test
    void cancelOrder_ShouldThrowException_WhenOrderDoesNotExist() {

        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.cancelOrder(orderId)
                );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }

    @Test
    void checkout_ShouldCreateOrderAndClearCart_WhenCartHasItems() {

        Long userId = 1L;

        User user = new User();

        Product product = new Product();
        product.setPrice(new BigDecimal("100.00"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(cartItem);

        Order savedOrder = new Order();

        OrderResponseDTO responseDTO =
                new OrderResponseDTO();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.of(cart));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        when(orderMapper.toDTO(savedOrder))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.checkout(userId);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(userRepository).findById(userId);
        verify(cartRepository).findByUserId(userId);
        verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        assertEquals(user, capturedOrder.getUser());
        assertEquals(OrderStatus.CONFIRMED, capturedOrder.getStatus());
        assertEquals(new BigDecimal("200.00"), capturedOrder.getTotalAmount());

        assertEquals(1, capturedOrder.getOrderItems().size());

        OrderItem capturedOrderItem =
                capturedOrder.getOrderItems().get(0);

        assertEquals(product, capturedOrderItem.getProduct());
        assertEquals(2, capturedOrderItem.getQuantity());
        assertEquals(new BigDecimal("100.00"), capturedOrderItem.getPrice());
        assertEquals(capturedOrder, capturedOrderItem.getOrder());
        verify(cartRepository).save(cart);
        verify(orderMapper).toDTO(savedOrder);

        assertTrue(cart.getCartItems().isEmpty());
    }


    @Test
    void checkout_ShouldThrowException_WhenUserDoesNotExist() {

        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.checkout(userId)
                );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verifyNoInteractions(cartRepository);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }


    @Test
    void checkout_ShouldThrowException_WhenCartDoesNotExist() {

        Long userId = 1L;

        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.checkout(userId)
                );

        assertEquals("Cart not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(cartRepository).findByUserId(userId);

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }


    @Test
    void checkout_ShouldThrowException_WhenCartIsEmpty() {

        Long userId = 1L;

        User user = new User();

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.of(cart));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> orderService.checkout(userId)
                );

        assertEquals("Cart is empty", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(cartRepository).findByUserId(userId);

        verify(orderRepository, never()).save(any());
        verify(cartRepository, never()).save(any());
        verifyNoInteractions(orderMapper);
    }

}
