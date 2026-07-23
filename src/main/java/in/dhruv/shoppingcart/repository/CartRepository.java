package in.dhruv.shoppingcart.repository;

import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(Long userId);
}
