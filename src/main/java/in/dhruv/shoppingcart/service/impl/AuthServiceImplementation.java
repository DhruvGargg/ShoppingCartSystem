package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.auth.AuthResponseDTO;
import in.dhruv.shoppingcart.dto.auth.LoginRequestDTO;
import in.dhruv.shoppingcart.dto.auth.RegisterRequestDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.enums.Role;
import in.dhruv.shoppingcart.exception.ResourceNotFoundException;
import in.dhruv.shoppingcart.repository.CartRepository;
import in.dhruv.shoppingcart.repository.UserRepository;
import in.dhruv.shoppingcart.security.CustomUserDetails;
import in.dhruv.shoppingcart.security.JWTService;
import in.dhruv.shoppingcart.service.AuthService;
import in.dhruv.shoppingcart.service.CartService;
import in.dhruv.shoppingcart.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JWTService jwtService;

    public AuthServiceImplementation(
            UserRepository userRepository,
            CartRepository cartRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationProvider authenticationProvider,
            JWTService jwtService
    ) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    @Override
    public void register(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new ResourceNotFoundException("User with this email already exists");
        }
        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setEnabled(true);
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication =
                authenticationProvider.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.getEmail(),
                                loginRequestDTO.getPassword()
                        )
                );
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        String token =
                jwtService.generateToken(userDetails);
        return new AuthResponseDTO(token);
    }
}
