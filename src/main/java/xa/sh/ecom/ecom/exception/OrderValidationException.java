package xa.sh.ecom.ecom.exception;

public class OrderValidationException extends ApiException {
    public OrderValidationException(String message) {
        super("Order validation failed: " + message);
    }
}
