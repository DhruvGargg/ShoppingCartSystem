package in.dhruv.shoppingcart.dto.auth;

import in.dhruv.shoppingcart.enums.Role;

public class LoginResponse {

    private String token;
    private String type;
    private String username;
    private Role role;
}
