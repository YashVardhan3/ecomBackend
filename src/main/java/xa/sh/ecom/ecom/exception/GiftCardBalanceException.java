package xa.sh.ecom.ecom.exception;

public class GiftCardBalanceException extends ApiException {
    public GiftCardBalanceException() {
        super("Gift card has insufficient balance");
    }
}
