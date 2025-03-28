package xa.sh.ecom.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends ApiException {
    public AccessDeniedException(String string) {
        super("You don't have permission to access this resource");
    }

}
