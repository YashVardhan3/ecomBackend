package xa.sh.ecom.ecom.models;

// xa/sh/ecom/ecom/config/DataInitializer.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import xa.sh.ecom.ecom.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        createRoleIfNotFound(ERole.ROLE_USER);
        createRoleIfNotFound(ERole.ROLE_SELLER);
        createRoleIfNotFound(ERole.ROLE_ADMIN);
    }

    private void createRoleIfNotFound(ERole roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }
}