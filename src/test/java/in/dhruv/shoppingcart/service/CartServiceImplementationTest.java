package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.cart.UpdateCartResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.CartItem;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.mapper.CartMapper;
import in.dhruv.shoppingcart.repository.CartItemRepository;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.ProductRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import in.dhruv.shoppingcart.service.impl.CartServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImplementation  cartServiceImplementation;

    @Test
    void createCartShouldThrowExceptionIfCartAlreadyExists()
    {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(1L))
        .thenReturn(Optional.of(cart));

        RuntimeException exception =
                assertThrows(
                    RuntimeException.class,
                        () -> cartServiceImplementation.createCart(1L)
                );

        assertEquals(
                "User already has a cart",
                exception.getMessage()
        );

        verify(cartRepository, never()).save(any());
    }

    @Test
    void createCartShouldCreateCartSuccessfully()
    {
        User user = new User();
        user.setId(1L);

        UpdateCartResponseDTO updateCartResponseDTO = new UpdateCartResponseDTO();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        when(cartMapper.toResponseDTO(any(Cart.class)))
        .thenReturn(updateCartResponseDTO);

        UpdateCartResponseDTO result =
                cartServiceImplementation.createCart(1L);

        assertNotNull(result);

        verify(cartRepository).save(any(Cart.class));

        verify(cartMapper).toResponseDTO(any(Cart.class));
    }

    @Test
    void createCartShouldThrowExceptionWhenUserNotFound()
    {
        when(userRepository.findById(1L))
        .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> cartServiceImplementation.createCart(1L)
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(cartRepository, never())
                .save(any(Cart.class));
    }

    @Test
    void getCartByUserIdShouldReturnCart()
    {
        Cart cart =  new Cart();
        cart.setId(1L);

        UpdateCartResponseDTO updateCartResponseDTO = new UpdateCartResponseDTO();

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(cartMapper.toResponseDTO(any(Cart.class)))
        .thenReturn(updateCartResponseDTO);

        UpdateCartResponseDTO result =
                cartServiceImplementation.getCartByUserId(1L);

        assertNotNull(result);

        verify(cartRepository).findByUserId(1L);

        verify(cartMapper).toResponseDTO(cart);
    }

    @Test
    void getCartByUserIdShouldThrowExceptionWhenCartDoesNotExist()
    {
        Long userId = 1L;

        when(cartRepository.findByUserId(1L))
        .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartServiceImplementation.getCartByUserId(userId)
        );

        assertEquals(
                "Cart not found",
                exception.getMessage()
        );

        verify(cartMapper, never())
                .toResponseDTO(any(Cart.class));
    }

    @Test
    void clearCartShouldRemoveAllItems()
    {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setCartItems(
                new ArrayList<>(
                        List.of(
                                new CartItem(),
                                new CartItem()
                        )
                )
        );

        cart.setTotalPrice(BigDecimal.TEN);

        when(cartRepository.findByUserId(userId))
        .thenReturn(Optional.of(cart));

        cartServiceImplementation.clearCart(userId);

        assertTrue(
                cart.getCartItems().isEmpty()
        );

        assertEquals(
                BigDecimal.ZERO,
                cart.getTotalPrice()
        );

        verify(cartRepository).save(cart);
    }

    @Test
    void clearCartShouldThrowExceptionWhenCartDoesNotExist()
    {
        Long userId = 1L;

        when(cartRepository.findByUserId(userId))
        .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartServiceImplementation.clearCart(userId)
        );

        assertEquals(
                "Cart not found",
                exception.getMessage()
        );

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void removeProductFromCartShouldRemoveProductSuccessfully()
    {
        Long userId = 1L;
        Long productId = 2L;

        Cart cart = new Cart();
        cart.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);

        cart.setCartItems(List.of(cartItem));

        UpdateCartResponseDTO updateCartResponseDTO = new UpdateCartResponseDTO();

        when(cartRepository.findByUserId(1L))
        .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndProductId(
                cart.getId(),
                productId
        ))
        .thenReturn(Optional.of(cartItem));

        when(cartMapper.toResponseDTO(any(Cart.class)))
        .thenReturn(updateCartResponseDTO);

        UpdateCartResponseDTO result =
                cartServiceImplementation.removeProductFromCart(userId, productId);

        assertNotNull(result);

        assertTrue(cart.getCartItems().isEmpty());

        verify(cartItemRepository).delete(cartItem);

        verify(cartRepository).save(cart);
    }
}
