package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.auth.AuthResponseDTO;
import in.dhruv.shoppingcart.dto.auth.LoginRequestDTO;
import in.dhruv.shoppingcart.dto.auth.RegisterRequestDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.enums.Role;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import in.dhruv.shoppingcart.security.CustomUserDetails;
import in.dhruv.shoppingcart.security.JWTService;
import in.dhruv.shoppingcart.service.impl.AuthServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JWTService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails customUserDetails;

    @InjectMocks
    private AuthServiceImplementation authService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {

        registerRequestDTO = new RegisterRequestDTO(
                "Dhruv",
                "dhruv@gmail.com",
                "password123"
        );

        loginRequestDTO = new LoginRequestDTO(
                "dhruv@gmail.com",
                "password123"
        );
    }

    @Test
    void register_ShouldRegisterUserAndCreateCart() {

        // Arrange
        when(userRepository.existsByEmail(registerRequestDTO.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Dhruv");
        savedUser.setEmail("dhruv@gmail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        authService.register(registerRequestDTO);

        // Assert
        verify(userRepository).existsByEmail(registerRequestDTO.getEmail());

        verify(passwordEncoder)
                .encode(registerRequestDTO.getPassword());

        verify(userRepository)
                .save(any(User.class));

        verify(cartRepository)
                .save(any(Cart.class));
    }

    @Test
    void register_ShouldSetCorrectUserDetails() {

        // Arrange
        when(userRepository.existsByEmail(registerRequestDTO.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        authService.register(registerRequestDTO);

        // Assert
        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUserArgument = userCaptor.getValue();

        assertEquals("Dhruv", savedUserArgument.getName());
        assertEquals(
                "dhruv@gmail.com",
                savedUserArgument.getEmail()
        );
        assertEquals(
                "encodedPassword",
                savedUserArgument.getPassword()
        );
        assertEquals(Role.USER, savedUserArgument.getRole());
        assertTrue(savedUserArgument.getEnabled());

        assertNotNull(savedUserArgument.getCreatedAt());
        assertNotNull(savedUserArgument.getUpdatedAt());
    }

    @Test
    void register_ShouldCreateCartWithZeroTotalPrice() {

        // Arrange
        when(userRepository.existsByEmail(registerRequestDTO.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        authService.register(registerRequestDTO);

        // Assert
        ArgumentCaptor<Cart> cartCaptor =
                ArgumentCaptor.forClass(Cart.class);

        verify(cartRepository).save(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();

        assertEquals(savedUser, savedCart.getUser());
        assertEquals(
                BigDecimal.ZERO,
                savedCart.getTotalPrice()
        );
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {

        // Arrange
        when(userRepository.existsByEmail(registerRequestDTO.getEmail()))
                .thenReturn(true);

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.register(registerRequestDTO)
                );

        assertEquals(
                "User with this email already exists",
                exception.getMessage()
        );

        verify(userRepository)
                .existsByEmail(registerRequestDTO.getEmail());

        verify(userRepository, never())
                .save(any(User.class));

        verify(cartRepository, never())
                .save(any(Cart.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void login_ShouldReturnAuthResponseWithGeneratedToken() {

        // Arrange
        String token = "jwt-token";

        when(authenticationProvider.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(customUserDetails);

        when(jwtService.generateToken(customUserDetails))
                .thenReturn(token);

        // Act
        AuthResponseDTO response =
                authService.login(loginRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(token, response.getToken());

        verify(authenticationProvider)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));

        verify(jwtService)
                .generateToken(customUserDetails);
    }

    @Test
    void login_ShouldAuthenticateUsingCorrectCredentials() {

        // Arrange
        when(authenticationProvider.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(customUserDetails);

        when(jwtService.generateToken(customUserDetails))
                .thenReturn("jwt-token");

        // Act
        authService.login(loginRequestDTO);

        // Assert
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(
                        UsernamePasswordAuthenticationToken.class
                );

        verify(authenticationProvider)
                .authenticate(captor.capture());

        UsernamePasswordAuthenticationToken token =
                captor.getValue();

        assertEquals(
                "dhruv@gmail.com",
                token.getPrincipal()
        );

        assertEquals(
                "password123",
                token.getCredentials()
        );
    }
}