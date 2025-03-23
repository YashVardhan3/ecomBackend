package xa.sh.ecom.ecom.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.order.models.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{

}
