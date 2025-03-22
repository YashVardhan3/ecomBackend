// xa/sh/ecom/ecom/repository/RoleRepository.java
package xa.sh.ecom.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xa.sh.ecom.ecom.models.ERole;
import xa.sh.ecom.ecom.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}