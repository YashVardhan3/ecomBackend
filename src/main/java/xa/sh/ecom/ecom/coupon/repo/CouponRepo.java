package xa.sh.ecom.ecom.coupon.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.coupon.models.Coupon;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    Boolean existsByCode(String code);
}
