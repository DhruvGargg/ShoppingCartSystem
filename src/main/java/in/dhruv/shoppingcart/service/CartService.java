package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.cart.CartResponse;
import in.dhruv.shoppingcart.entity.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);
    Cart createCart(Long userId);
    Cart addProductToCart(Long userId, Long productId, Integer quantity);
    Cart updateCartItemQuantity(
            Long userId,
            Long productId,
            Integer quantity
    );
    Cart removeProductFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
