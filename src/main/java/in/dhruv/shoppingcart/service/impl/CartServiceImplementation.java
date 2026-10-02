package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.cart.AddToCartRequestDTO;
import in.dhruv.shoppingcart.dto.cart.UpdateCartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.CartItem;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.mapper.CartMapper;
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
    private final CartMapper cartMapper;

    public CartServiceImplementation(
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            CartItemRepository cartItemRepository,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public UpdateCartResponseDTO getCartByUserId(
            Long userId
    ) {
        Cart cart = findCartByUserId(userId);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public UpdateCartResponseDTO createCart(
            Long userId
    ) {
        User user = findUserByUserId(userId);

        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("User already has a cart");
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    @Transactional
    public UpdateCartResponseDTO addProductToCart(
            AddToCartRequestDTO addToCartRequestDTO,
            Long userId
    ) {
        if (addToCartRequestDTO.getQuantity() <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }
        Cart cart = findCartByUserId(
                userId
        );
        Product product = findProductByProductId(
                addToCartRequestDTO.getProductId()
        );
        CartItem cartItem = findCartItemByCartIdAndProductId(
                cart.getId(),
                addToCartRequestDTO.getProductId()
        );
        if (cartItem == null) {
            if (product.getStock() < addToCartRequestDTO.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient product stock"
                );
            }
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(addToCartRequestDTO.getQuantity());
            cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(addToCartRequestDTO.getQuantity())));
            cart.getCartItems().add(cartItem);
        } else {
            int newQuantity = cartItem.getQuantity() + addToCartRequestDTO.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new RuntimeException(
                        "Insufficient product stock"
                );
            }
            cartItem.setQuantity(newQuantity);
            cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        }
        cartItemRepository.save(cartItem);
        calculateCartTotal(cart);
        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    @Transactional
    public UpdateCartResponseDTO updateCartItemQuantity(
            Long userId,
            Long productId,
            Integer quantity
    ) {
        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }
        Cart cart = findCartByUserId(userId);

        CartItem cartItem = findCartItemByCartIdAndProductId(cart.getId(), productId);
        Product product = cartItem.getProduct();
        if (product.getStock() < quantity) {
            throw new RuntimeException(
                    "Insufficient product stock"
            );
        }
        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItemRepository.save(cartItem);
        calculateCartTotal(cart);
        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    @Transactional
    public UpdateCartResponseDTO removeProductFromCart (
            Long userId,
            Long productId
    )
    {
        Cart cart = findCartByUserId(userId);
        CartItem cartItem = cartItemRepository.
                findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        calculateCartTotal(cart);
        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    @Transactional
    public void clearCart (Long userId) {
        Cart cart = findCartByUserId(userId);
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private void calculateCartTotal(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems()
                .stream()
                .map(CartItem::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
        cart.setTotalPrice(totalPrice);
    }

    private Cart findCartByUserId(
            Long userId
    ) {
        return cartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private User findUserByUserId(
            Long userId
    ) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Product findProductByProductId(
            Long productId
    ) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private CartItem findCartItemByCartIdAndProductId(
            Long cartId,
            Long productId
    ) {
        return cartItemRepository
                .findByCartIdAndProductId(cartId, productId)
                .orElse(null);
    }
}
