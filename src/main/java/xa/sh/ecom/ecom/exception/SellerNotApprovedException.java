package xa.sh.ecom.ecom.exception;

public class SellerNotApprovedException extends ApiException {
    public SellerNotApprovedException() {
        super("Seller account not approved");
    }
}
