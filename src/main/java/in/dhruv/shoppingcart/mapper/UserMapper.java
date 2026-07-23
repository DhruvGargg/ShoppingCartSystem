package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.user.UserRequestDTO;
import in.dhruv.shoppingcart.dto.user.UserResponseDTO;
import in.dhruv.shoppingcart.entity.User;
import org.springframework.stereotype.Component;

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

    public UserResponseDTO toResponseDTO(User user)
    {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setEnabled(user.getEnabled());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setUpdatedAt(user.getUpdatedAt());
        return userResponseDTO;
    }
}
