package in.dhruv.shoppingcart.dto.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private boolean success;

    private String message;

    private T data;
}
