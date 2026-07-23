package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.entity.User;

public interface AuthService {

    void register();
    void login();
    User getCurrentUser();
}
