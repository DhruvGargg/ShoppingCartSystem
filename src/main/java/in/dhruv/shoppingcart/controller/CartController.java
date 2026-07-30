package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.cart.CartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.security.CustomUserDetails;
import in.dhruv.shoppingcart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("create/user/{userId}")
    private ResponseEntity<CartResponseDTO> createCart(
            @PathVariable Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.createCart(userId)
        );
    }

    private ResponseEntity<CartResponseDTO> getMyCart(
            Authentication authentication
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        return ResponseEntity.ok(
                cartService.getCartByUserId(userId)
        );
    }

    @GetMapping("user/{userId}")
    private ResponseEntity<CartResponseDTO> getCartByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                cartService
                        .getCartByUserId(userId)
        );
    }

    @PostMapping("user/{userId}/product/{productId}")
    private ResponseEntity<CartResponseDTO> addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService
                        .addProductToCart(
                                userId,
                                productId,
                                quantity)
                );
    }

    @PatchMapping("user/{userId}/items/{productId}")
    private ResponseEntity<CartResponseDTO> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            Integer quantity
    ) {
        return ResponseEntity.ok(
                cartService
                        .updateCartItemQuantity(
                                userId,
                                productId,
                                quantity
                        )
        );
    }

    @DeleteMapping("user/{userId}/product/{productId}")
    private ResponseEntity<CartResponseDTO> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                cartService
                        .removeProductFromCart(
                                userId,
                                productId)
        );
    }

    @DeleteMapping("/user/{userId}/items")
    private ResponseEntity<Cart> clearCart(
            @PathVariable Long userId
    ) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
