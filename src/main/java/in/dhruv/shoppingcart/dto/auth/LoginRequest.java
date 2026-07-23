package in.dhruv.shoppingcart.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @Email
    private String email;

    @NotBlank
    private String password;
}
