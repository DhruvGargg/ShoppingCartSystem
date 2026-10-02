package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.cartitem.CartItemResponseDTO;
import in.dhruv.shoppingcart.dto.cartitem.CartItemRequestDTO;
import in.dhruv.shoppingcart.entity.CartItem;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.repository.ProductRepository;

public class CartItemMapper {

    ProductRepository productRepository;

    public CartItem toEntity(CartItemRequestDTO cartItemRequestDTO)
    {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemRequestDTO.getQuantity());
        Product product = productRepository
                .findById(cartItemRequestDTO.getProductId())
                .orElseGet(null);
        cartItem.setProduct(product);
        return cartItem;
    }

    public CartItemResponseDTO toResponseDTO(CartItem cartItem)
    {
        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setPrice(cartItem.getProduct().getPrice());
        cartItemResponseDTO.setQuantity(cartItem.getQuantity());
        cartItemResponseDTO.setSubtotal(cartItem.getSubtotal());
        return cartItemResponseDTO;
    }
}
