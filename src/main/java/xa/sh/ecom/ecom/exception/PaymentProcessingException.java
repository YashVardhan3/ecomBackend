package xa.sh.ecom.ecom.exception;

public class PaymentProcessingException extends ApiException {
    public PaymentProcessingException() {
        super("Payment processing failed");
    }
}