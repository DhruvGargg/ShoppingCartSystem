package in.dhruv.shoppingcart.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private Long cartId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalPrice;
}
