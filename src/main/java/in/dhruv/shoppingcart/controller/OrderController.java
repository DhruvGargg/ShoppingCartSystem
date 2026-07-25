package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.OrderDTO;
import in.dhruv.shoppingcart.enums.OrderStatus;
import in.dhruv.shoppingcart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderDTO> checkout(@PathVariable Long userId) {
        return ResponseEntity.ok(
                orderService
                        .checkout(userId)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(
                orderService
                        .getOrderById(orderId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                orderService
                        .getOrdersByUser(userId)
        );
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestParam OrderStatus status) {
        return ResponseEntity.ok(
                orderService
                        .updateOrderStatus(orderId, status)
        );
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(
                orderService
                        .cancelOrder(orderId)
        );
    }

}
