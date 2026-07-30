package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.cart.CartItemResponseDTO;
import in.dhruv.shoppingcart.entity.CartItem;

public class CartItemMapper {

    public CartItemResponseDTO toResponseDTO(CartItem cartItem)
    {
        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setPrice(cartItem.getProduct().getPrice());
        cartItemResponseDTO.setQuantity(cartItem.getQuantity());
        cartItemResponseDTO.setSubtotal(cartItem.getSubtotal());
        return cartItemResponseDTO;
    }
}
