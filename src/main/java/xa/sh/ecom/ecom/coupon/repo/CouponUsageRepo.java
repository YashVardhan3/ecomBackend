package xa.sh.ecom.ecom.coupon.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.coupon.models.CouponUsage;

@Repository
public interface CouponUsageRepo extends JpaRepository<CouponUsage, Long>{
    CouponUsage findByUserIdAndCouponId(Long userId, Long couponId);
}
