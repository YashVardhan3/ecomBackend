package xa.sh.ecom.ecom.coupon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.ecom.ecom.coupon.models.Coupon;
import xa.sh.ecom.ecom.coupon.models.CouponUsage;
import xa.sh.ecom.ecom.coupon.repo.CouponRepo;
import xa.sh.ecom.ecom.coupon.repo.CouponUsageRepo;
import xa.sh.ecom.ecom.exception.InvalidCouponException;
import xa.sh.ecom.ecom.order.repo.OrderRepo;

@Service
public class CouponServiceImpl {

    @Autowired
    private CouponRepo couponRepository;

    @Autowired
    private CouponUsageRepo couponUsageRepository;

    @Autowired
    private OrderRepo orderRepository;

    public CouponValidationResult validateCoupon(Long userId, String code, BigDecimal orderTotal) throws InvalidCouponException {
        Coupon coupon = couponRepository.findByCode(code);
                            //.orElseThrow(()->(new InvalidCouponException("test")));
            //.orElseThrow(() -> new InvalidCouponException("Invalid coupon code"));
        if (coupon==null) {
            throw new InvalidCouponException("Invalid coupon code");
        }
        // Basic validations
        validateCouponStatus(coupon);
        validateCouponUsageLimits(userId, coupon);
        
        // Calculate discount
        BigDecimal discount = calculateDiscount(coupon, orderTotal);
        
        return new CouponValidationResult(
            coupon,
            discount,
            orderTotal.add(discount.negate())
        );
    }

    private void validateCouponStatus(Coupon coupon) throws InvalidCouponException {
        if (!coupon.getIsActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        if (coupon.getValidUntil() != null && coupon.getValidUntil().isBefore(LocalDate.now())) {
            throw new InvalidCouponException("Coupon has expired");
        }
    }

    private void validateCouponUsageLimits(Long userId, Coupon coupon) throws InvalidCouponException {
        // Check global usage limits
        if (coupon.getMaxUses() != null && coupon.getUsedCount() >= coupon.getMaxUses()) {
            throw new InvalidCouponException("Coupon usage limit reached");
        }

        // Check per-user usage limits
        CouponUsage usage = couponUsageRepository.findByUserIdAndCouponId(userId, coupon.getId());
            //.orElse(new CouponUsage(userId, coupon));
            if (usage==null) {
                usage = new CouponUsage(userId, coupon);
            }
            
        if (usage.getUsageCount() >= coupon.getAllowedUsesPerUser()) {
            throw new InvalidCouponException("You've exceeded usage limit for this coupon");
        }
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderTotal) {
        return switch (coupon.getDiscountType()) {
            case PERCENTAGE -> orderTotal.multiply((coupon.getDiscountValue().divide(BigDecimal.valueOf(100)))).min(coupon.getMaxDiscount());
            case FIXED -> coupon.getDiscountValue().min(orderTotal);
        };
    }
}