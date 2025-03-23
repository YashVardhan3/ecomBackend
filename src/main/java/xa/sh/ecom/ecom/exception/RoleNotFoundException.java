package xa.sh.ecom.ecom.exception;

public class RoleNotFoundException extends ApiException {
    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }
}
