package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.auth.AuthResponseDTO;
import in.dhruv.shoppingcart.dto.auth.LoginRequestDTO;
import in.dhruv.shoppingcart.dto.auth.RegisterRequestDTO;
import in.dhruv.shoppingcart.entity.User;

public interface AuthService {

    void register(RegisterRequestDTO registerRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
