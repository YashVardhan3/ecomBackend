package xa.sh.ecom.ecom.product.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.product.models.Product;



@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByDescription(String description);
    // Page<Product> searchByNameOrDescription(String query, Pageable pageable);
    Product findByIdAndSellerId(Long id, Long sellerId);
    // Page<Product> findBySellerId(Long sellerId, Pageable pageable);
}
