package xa.sh.ecom.ecom.coupon.service.impl;

import java.math.BigDecimal;

import lombok.Data;
import xa.sh.ecom.ecom.coupon.models.Coupon;

@Data
public class CouponValidationResult {
    private Coupon coupon;
    private BigDecimal discount;
    private BigDecimal newOrderTotal;

    public CouponValidationResult(Coupon coupon, BigDecimal discount, BigDecimal newOrderTotal) {
        this.coupon = coupon;
        this.discount = discount;
        this.newOrderTotal = newOrderTotal;
    }

    // Getters and setters
}