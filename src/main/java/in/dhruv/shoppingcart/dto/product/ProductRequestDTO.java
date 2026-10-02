package in.dhruv.shoppingcart.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock must be non-negative")
    private Integer stock;

    private Long categoryId;
}
