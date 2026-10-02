package in.dhruv.shoppingcart.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequestDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}
