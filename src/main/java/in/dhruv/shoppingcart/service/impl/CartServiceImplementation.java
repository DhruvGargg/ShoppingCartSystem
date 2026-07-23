package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.CartItem;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.repository.CartItemRepository;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.ProductRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import in.dhruv.shoppingcart.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImplementation(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
    }

    @Override
    public Cart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("User already has a cart");
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.
                findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) {
            throw new RuntimeException(
                    "Insufficient product stock"
            );
        }
        CartItem cartItem = cartItemRepository.
                findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElse(null);
        if(cartItem == null)
        {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getCartItems().add(cartItem);
        }
        else
        {
            int newQuantity = cartItem.getQuantity() + quantity;
            if(product.getStock() < newQuantity)
            {
                throw new RuntimeException(
                        "Insufficient product stock"
                );
            }
            cartItem.setQuantity(newQuantity);
            cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        }
        cartItemRepository.save(cartItem);
        
        return cart;
    }

    @Override
    public Cart updateCartItemQuantity(Long userId, Long productId, Integer quantity) {
        return null;
    }

    @Override
    public Cart removeProductFromCart(Long userId, Long productId) {
        return null;
    }

    @Override
    public void clearCart(Long userId) {

    }
}
