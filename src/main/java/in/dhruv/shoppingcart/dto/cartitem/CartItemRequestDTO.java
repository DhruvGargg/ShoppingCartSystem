package in.dhruv.shoppingcart.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
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
public class CartItemRequestDTO {

    @NotNull(message = "Product id cannot be null")
    private Long productId;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
