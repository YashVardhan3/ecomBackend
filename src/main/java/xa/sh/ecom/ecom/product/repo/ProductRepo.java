package xa.sh.ecom.ecom.product.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.product.models.Product;



@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByDescription(String description);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:searchTerm% OR p.description LIKE %:searchTerm%")
    Page<Product> searchByNameOrDescription(@Param("searchTerm") String query, Pageable pageable);
    Product findByIdAndSellerId(Long id, Long sellerId);
    @Query("SELECT p FROM Product p WHERE p.seller.id=:sellerId")
    Page<Product> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id=:categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
