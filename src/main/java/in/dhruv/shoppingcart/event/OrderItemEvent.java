package in.dhruv.shoppingcart.event;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {

}
