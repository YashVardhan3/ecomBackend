package xa.sh.ecom.ecom.cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.cart.models.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

}
