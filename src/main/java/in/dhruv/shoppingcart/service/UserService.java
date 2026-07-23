package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
