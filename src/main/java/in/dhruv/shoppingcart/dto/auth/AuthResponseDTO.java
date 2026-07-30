package in.dhruv.shoppingcart.dto.auth;

import in.dhruv.shoppingcart.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
}
