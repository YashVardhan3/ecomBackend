package xa.sh.ecom.ecom.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.product.models.Category;

@Repository 
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
