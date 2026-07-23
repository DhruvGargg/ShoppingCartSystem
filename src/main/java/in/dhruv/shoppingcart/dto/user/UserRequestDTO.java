package in.dhruv.shoppingcart.dto.user;

import in.dhruv.shoppingcart.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private Role role;
}
