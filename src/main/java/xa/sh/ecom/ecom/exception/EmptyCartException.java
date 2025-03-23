package xa.sh.ecom.ecom.exception;

public class EmptyCartException extends ApiException {
    public EmptyCartException() {
        super("Cannot create order from empty cart");
    }
}
