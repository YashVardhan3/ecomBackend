package xa.sh.ecom.ecom.exception;

public class InvalidGiftCardException extends ApiException {
    public InvalidGiftCardException(String message) {
        super("Invalid gift card: " + message);
    }
}
