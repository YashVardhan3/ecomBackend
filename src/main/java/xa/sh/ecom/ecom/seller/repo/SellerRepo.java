package xa.sh.ecom.ecom.seller.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.seller.models.Seller;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {

}
