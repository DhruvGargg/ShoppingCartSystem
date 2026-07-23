package in.dhruv.shoppingcart.dto.cart;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;

    private BigDecimal price;
    private BigDecimal subtotal;
}
