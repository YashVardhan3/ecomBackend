package xa.sh.ecom.ecom.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.order.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

}
