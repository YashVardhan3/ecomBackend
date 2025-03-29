package xa.sh.ecom.ecom.coupon.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.coupon.models.CouponUsage;

@Repository
public interface CouponUsageRepo extends JpaRepository<CouponUsage, Long>{
    Optional<CouponUsage> findByUserIdAndCouponId(Long userId, Long couponId);
}
