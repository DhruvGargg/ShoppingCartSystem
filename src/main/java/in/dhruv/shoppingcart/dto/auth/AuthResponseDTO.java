package in.dhruv.shoppingcart.dto.auth;

import in.dhruv.shoppingcart.enums.Role;

public class LoginResponseDTO {

    private String token;
    private String type;
    private String username;
    private Role role;
}
