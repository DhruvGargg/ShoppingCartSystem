package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.user.UserRequestDTO;
import in.dhruv.shoppingcart.dto.user.UserResponseDTO;
import in.dhruv.shoppingcart.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO userRequestDTO)
    {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        return user;
    }

    public UserResponseDTO toResponseDTO(UserRequestDTO userRequestDTO)
    {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName(userRequestDTO.getName());
        userResponseDTO.setEmail(userRequestDTO.getEmail());
        userResponseDTO.setRole(userRequestDTO.getRole());
        return userResponseDTO;
    }
}
