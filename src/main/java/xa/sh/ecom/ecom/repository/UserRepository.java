// xa/sh/ecom/ecom/repository/UserRepository.java
package xa.sh.ecom.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xa.sh.ecom.ecom.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}