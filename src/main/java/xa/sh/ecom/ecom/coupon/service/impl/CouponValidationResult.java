package xa.sh.ecom.ecom.coupon.service.impl;

import lombok.Data;
import xa.sh.ecom.ecom.coupon.models.Coupon;

@Data
public class CouponValidationResult {
    private Coupon coupon;
    private double discount;
    private double newOrderTotal;

    public CouponValidationResult(Coupon coupon, double discount, double newOrderTotal) {
        this.coupon = coupon;
        this.discount = discount;
        this.newOrderTotal = newOrderTotal;
    }

    // Getters and setters
}