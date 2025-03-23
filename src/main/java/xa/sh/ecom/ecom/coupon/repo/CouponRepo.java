package xa.sh.ecom.ecom.coupon.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.coupon.models.Coupon;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);
}
