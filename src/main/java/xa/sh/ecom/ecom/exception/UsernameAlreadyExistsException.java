package xa.sh.ecom.ecom.exception;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException() {
        super("Username is already taken");
    }
}