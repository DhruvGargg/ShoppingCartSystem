package in.dhruv.shoppingcart.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank
    private String name;
    private String description;

    @PositiveOrZero
    private BigDecimal price;

    @Min(1)
    private Integer stock;
    private Long categoryId;
}
