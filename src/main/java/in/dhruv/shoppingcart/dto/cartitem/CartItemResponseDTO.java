package in.dhruv.shoppingcart.dto.cartitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private Long productId;
    private String productName;
    private Integer quantity;

    private BigDecimal price;
    private BigDecimal subtotal;
}
