package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.cart.AddToCartRequestDTO;
import in.dhruv.shoppingcart.dto.cart.UpdateCartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.CartItem;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.exception.ResourceNotFoundException;
import in.dhruv.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    ProductRepository productRepository;

    public Cart toEntity(
            AddToCartRequestDTO addToCartRequestDTO
    ) {
        Cart cart = new Cart();
        Product product = productRepository
                .findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(addToCartRequestDTO.getQuantity());
        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice());
        return cart;
    }

    public UpdateCartResponseDTO toResponseDTO (
            Cart cart
    ) {
        UpdateCartResponseDTO updateCartResponseDTO = new UpdateCartResponseDTO();
        updateCartResponseDTO.setItems(updateCartResponseDTO.getItems());
        updateCartResponseDTO.setCartId(cart.getId());
        updateCartResponseDTO.setTotalPrice(cart.getTotalPrice());
        return updateCartResponseDTO;
    }
}
