package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.cart.AddToCartRequestDTO;
import in.dhruv.shoppingcart.dto.cart.UpdateCartResponseDTO;

public interface CartService {

    UpdateCartResponseDTO getCartByUserId(Long userId);
    UpdateCartResponseDTO createCart(Long userId);
    UpdateCartResponseDTO addProductToCart(
            AddToCartRequestDTO addToCartRequestDTO,
            Long userId
    );
    UpdateCartResponseDTO updateCartItemQuantity(
            Long userId,
            Long productId,
            Integer quantity
    );
    UpdateCartResponseDTO removeProductFromCart(Long userId,
                                                Long productId);
    void clearCart(Long userId);
}
