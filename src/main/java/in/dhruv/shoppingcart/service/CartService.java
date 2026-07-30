package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.cart.CartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import org.springframework.security.core.Authentication;

public interface CartService {

    CartResponseDTO getCartByUserId(Long userId);
    CartResponseDTO createCart(Long userId);
    CartResponseDTO addProductToCart(Long userId,
                          Long productId,
                          Integer quantity);
    CartResponseDTO updateCartItemQuantity(
            Long userId,
            Long productId,
            Integer quantity
    );
    CartResponseDTO removeProductFromCart(Long userId,
                               Long productId);
    void clearCart(Long userId);
}
