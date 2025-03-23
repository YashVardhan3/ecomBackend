package xa.sh.ecom.ecom.exception;

public class InvalidCouponException extends ApiException {
    public InvalidCouponException(String message) {
        super("Invalid coupon: " + message);
    }
}