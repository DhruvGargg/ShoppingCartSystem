package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.cart.CartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartResponseDTO toResponseDTO (
            Cart cart
    ) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setItems(cartResponseDTO.getItems());
        cartResponseDTO.setCartId(cart.getId());
        cartResponseDTO.setTotalPrice(cart.getTotalPrice());
        return cartResponseDTO;
    }
}
