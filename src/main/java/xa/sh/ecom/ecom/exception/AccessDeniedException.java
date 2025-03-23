package xa.sh.ecom.ecom.exception;

public class AccessDeniedException extends ApiException {
    public AccessDeniedException() {
        super("You don't have permission to access this resource");
    }
}
