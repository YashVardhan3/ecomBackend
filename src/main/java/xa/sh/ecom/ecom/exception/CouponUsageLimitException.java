package xa.sh.ecom.ecom.exception;

public class CouponUsageLimitException extends ApiException {
    public CouponUsageLimitException() {
        super("Coupon usage limit reached");
    }
}