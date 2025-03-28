package xa.sh.ecom.ecom.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import xa.sh.ecom.ecom.security.services.UserDetailsImpl; // Import your UserDetailsImpl

@Component
public final class SecurityUtils {

    private SecurityUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the UserDetailsImpl for the currently authenticated user.
     *
     * @return UserDetailsImpl of the current user.
     * @throws AccessDeniedException if no user is authenticated.
     * @throws IllegalStateException if the principal is not of the expected type.
     */
    public static UserDetailsImpl getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // Handle cases where authentication is null, not authenticated, or anonymous
            throw new AccessDeniedException("User is not authenticated.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            // This shouldn't happen with standard configuration but is a safeguard
            System.err.println("Unexpected principal type: " + principal.getClass().getName());
            throw new IllegalStateException("Unexpected principal type found in SecurityContext.");
        }
    }

    /**
     * Gets the ID (Long) of the currently authenticated user.
     *
     * @return The Long ID of the current user.
     * @throws AccessDeniedException if no user is authenticated.
     * @throws IllegalStateException if the principal is not of the expected type.
     */
    public static Long getAuthenticatedUserId() {
        return getAuthenticatedUserDetails().getId();
    }

    /**
     * Checks if the currently authenticated user's ID matches the given ID.
     * Throws NotAuthorizedException if they don't match.
     *
     * @param idToCheck The ID to compare against the authenticated user's ID.
          * @throws xa.sh.ecom.ecom.exception.AccessDeniedException 
          * @throws AccessDeniedException if no user is authenticated.
          * @throws IllegalStateException if the principal is not of the expected type.
          */
         public static void checkCurrentUserMatchesId(Long idToCheck) throws xa.sh.ecom.ecom.exception.AccessDeniedException {
        Long currentUserId = getAuthenticatedUserId();
        if (!currentUserId.equals(idToCheck)) {
            // You should have created this exception as suggested before
            throw new xa.sh.ecom.ecom.exception.AccessDeniedException(
                    "Authenticated user ID (" + currentUserId + ") does not match the required ID (" + idToCheck + ")");
        }
    }

    /**
     * Checks if the currently authenticated user has a specific role.
     *
     * @param roleName The name of the role (e.g., "ROLE_ADMIN", "ROLE_SELLER").
     * @return true if the user has the role, false otherwise.
     * @throws AccessDeniedException if no user is authenticated.
     */
    public static boolean hasRole(String roleName) {
        return getAuthenticatedUserDetails().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

}