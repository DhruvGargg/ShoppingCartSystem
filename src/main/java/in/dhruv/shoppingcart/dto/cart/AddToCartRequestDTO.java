package in.dhruv.shoppingcart.dto.cart;

import jakarta.validation.constraints.Min;

public class AddToCartRequestDTO {

    private Long productId;

    @Min(1)
    private Integer quantity;
}
