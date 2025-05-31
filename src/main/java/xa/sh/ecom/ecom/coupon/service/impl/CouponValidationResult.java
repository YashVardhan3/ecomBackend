package xa.sh.ecom.ecom.coupon.service.impl;

import java.math.BigDecimal;

import xa.sh.ecom.ecom.coupon.models.Coupon;

public class CouponValidationResult {
    private Coupon coupon;
    private BigDecimal discount;
    private BigDecimal newOrderTotal;

    public CouponValidationResult(Coupon coupon, BigDecimal discount, BigDecimal newOrderTotal) {
        this.coupon = coupon;
        this.discount = discount;
        this.newOrderTotal = newOrderTotal;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNewOrderTotal() {
        return newOrderTotal;
    }

    public void setNewOrderTotal(BigDecimal newOrderTotal) {
        this.newOrderTotal = newOrderTotal;
    }

    // Getters and setters
}