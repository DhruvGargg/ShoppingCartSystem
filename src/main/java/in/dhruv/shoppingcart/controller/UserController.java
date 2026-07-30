package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.auth.RegisterRequestDTO;
import in.dhruv.shoppingcart.dto.user.UserRequestDTO;
import in.dhruv.shoppingcart.dto.user.UserResponseDTO;
import in.dhruv.shoppingcart.entity.User;
import in.dhruv.shoppingcart.service.UserService;
import org.hibernate.annotations.ConcreteProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(registerRequestDTO));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                userService
                        .getUserById(id)
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(
                userService
                        .getUserByEmail(email)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return ResponseEntity.ok(
                userService.updateUser(id, user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
