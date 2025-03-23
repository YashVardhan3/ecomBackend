package xa.sh.ecom.ecom.order.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.order.models.Order;
import xa.sh.ecom.ecom.order.models.OrderItem;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>{
    List<OrderItem> findSellerOrderItems(Long sellerId, LocalDate startDate, LocalDate endDate);

}
