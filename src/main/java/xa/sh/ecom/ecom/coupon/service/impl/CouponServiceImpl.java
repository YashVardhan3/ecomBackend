package xa.sh.ecom.ecom.coupon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.ecom.ecom.coupon.models.Coupon;
import xa.sh.ecom.ecom.coupon.models.CouponUsage;
import xa.sh.ecom.ecom.coupon.repo.CouponRepo;
import xa.sh.ecom.ecom.coupon.repo.CouponUsageRepo;
import xa.sh.ecom.ecom.exception.InvalidCouponException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.order.repo.OrderRepo;
import xa.sh.ecom.ecom.repository.UserRepository;
@Service
public class CouponServiceImpl {

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CouponUsageRepo couponUsageRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepository userRepo;


    private static final Logger log = LoggerFactory.getLogger(CouponServiceImpl.class);


    @Transactional(readOnly = true)
    public CouponValidationResult validateCoupon(Long userId,String code, BigDecimal orderTotal) throws ResourceNotFoundException, InvalidCouponException{
        log.debug("Validation coupon code "+code +"for "+userId);

        Optional<Coupon> coupon = couponRepo.findByCode(code);
        if (coupon.isEmpty()) {
            throw new ResourceNotFoundException("coupon with this code not found "); 
        }

        validateCouponStatus(coupon.get());
        validateCouponUsageLimits(userId, coupon.get());

        BigDecimal discount = calculateDiscount(coupon, orderTotal);
        BigDecimal finalTotal = orderTotal.subtract(discount);

        return new CouponValidationResult(coupon.get(), discount, finalTotal);

    }

    public void validateCouponStatus(Coupon coupon) throws InvalidCouponException{
        if (coupon.getIsActive()==null||!coupon.getIsActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        if (coupon.getValidUntil()!=null&&coupon.getValidUntil().isBefore(LocalDate.now())) {
            throw new InvalidCouponException("Coupon is not expired");
        }
    }

    public void validateCouponUsageLimits(Long userId, Coupon coupon) throws InvalidCouponException{
        if (coupon.getMaxUses()!=null&&coupon.getUsedCount()!=null&&coupon.getUsedCount()>=coupon.getMaxUses()) {
            throw new InvalidCouponException("Coupon Limit reached");
        }
        // Optional<CouponUsage> couponUsageN = couponUsageRepo.findByUserIdAndCouponId(userId, coupon.getId());
        // CouponUsage couponUsage = couponUsageN.get();
        // int currentUsageCount = 0;
        // if (couponUsage!=null) {
        //     currentUsageCount = couponUsage.getUsageCount();

        // }
        int currentUsageCount = couponUsageRepo.findByUserIdAndCouponId(userId, coupon.getId())
        .map(CouponUsage::getUsageCount) // Get count if usage record exists
        .orElse(0); // Default to 0 if no usage record found


        int allowedUses = (coupon.getAllowedUsesPerUser() != null && coupon.getAllowedUsesPerUser() > 0)
                            ? coupon.getAllowedUsesPerUser() : 1;

        if (currentUsageCount >= allowedUses) {
                                throw new InvalidCouponException("You have already used this coupon the maximum number of times allowed.");
        }                    
    }

    private BigDecimal calculateDiscount(Optional<Coupon> couponN , BigDecimal orderTotal){
        Coupon coupon = couponN.get();
        if (orderTotal==null||orderTotal.compareTo(BigDecimal.ZERO)<=0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;

        switch (coupon.getDiscountType()) {
            case PERCENTAGE:
                if (coupon.getDiscountType()!=null) {
                    BigDecimal percentage = coupon.getDiscountValue().divide(new BigDecimal(100));
                    discount = orderTotal.multiply(percentage);
                }
                if (coupon.getMaxDiscount()!=null&&coupon.getMaxDiscount().compareTo(BigDecimal.ZERO)>0) {
                    discount = discount.min(coupon.getMaxDiscount());
                }
                break;
        
            case FIXED:
                if (coupon.getDiscountValue()!=null) {
                    discount = coupon.getDiscountValue().min(orderTotal);
                }
                if (coupon.getMaxDiscount()!=null&&coupon.getMaxDiscount().compareTo(BigDecimal.ZERO)>0) {
                    discount = discount.min(coupon.getMaxDiscount());
                }    
                break;
            default:
                    log.warn("Unknown discount type encountered");
                break;
        }

        discount  = discount.max(BigDecimal.ZERO);
        discount = discount.min(orderTotal);
        return discount;
    }


    @Transactional
    public void applyCouponUsage(Long userId, Long couponId) throws ResourceNotFoundException, InvalidCouponException {
        log.info("Applying usage for coupon ID {} to user ID {}", couponId, userId);
        Coupon coupon = couponRepo.findById(couponId)
             .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + couponId));

        // Re-validate limits just before applying (optional but safer against race conditions)
        validateCouponUsageLimits(userId, coupon);

        // Increment global usage count
        coupon.setUsedCount(coupon.getUsedCount() + 1);

        // Increment per-user usage count
        Optional<CouponUsage> usageN = couponUsageRepo.findByUserIdAndCouponId(userId, coupon.getId());
        CouponUsage usage = usageN.get();    
        if (usageN.isEmpty()) {
            //CouponUsage newUsage = new CouponUsage();
            // You need a way to get/create the User entity reference here
            // This might require injecting UserRepository or having a User reference passed in
            // For now, let's assume CouponUsage constructor handles fetching/setting User based on ID
             // *** THIS PART NEEDS REFINEMENT - How to set the User object? ***
            Optional<User> user = userRepo.findById(userId);
             usage.setUser(user.get()); // Assuming a setter like this exists, or adapt constructor
            usage.setCoupon(coupon);
            usage.setUsageCount(0); // Start count at 0 before incrementing
            }    

        usage.setUsageCount(usage.getUsageCount() + 1);

        // Save updated entities
        couponRepo.save(coupon);
        couponUsageRepo.save(usage);
        log.info("Coupon usage applied successfully for coupon ID {} and user ID {}", couponId, userId);
    }

}