package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.auth.AuthResponseDTO;
import in.dhruv.shoppingcart.dto.auth.LoginRequestDTO;
import in.dhruv.shoppingcart.dto.auth.LoginResponseDTO;
import in.dhruv.shoppingcart.dto.auth.RegisterRequestDTO;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.security.CustomUserDetails;
import in.dhruv.shoppingcart.security.JWTService;
import in.dhruv.shoppingcart.service.AuthService;
import in.dhruv.shoppingcart.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        authService.register(registerRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        return ResponseEntity.ok(
                authService.login(loginRequestDTO)
        );
    }
}
