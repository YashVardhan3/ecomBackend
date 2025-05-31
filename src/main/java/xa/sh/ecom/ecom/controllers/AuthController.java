// xa/sh/ecom/ecom/controller/AuthController.java
package xa.sh.ecom.ecom.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import xa.sh.ecom.ecom.models.ERole;
import xa.sh.ecom.ecom.models.Role;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.payload.request.LoginRequest;
import xa.sh.ecom.ecom.payload.request.SignupRequest;
import xa.sh.ecom.ecom.payload.response.JwtResponse;
import xa.sh.ecom.ecom.payload.response.MessageResponse;
import xa.sh.ecom.ecom.repository.RoleRepository;
import xa.sh.ecom.ecom.repository.UserRepository;
import xa.sh.ecom.ecom.security.jwt.JwtUtils;
import xa.sh.ecom.ecom.security.services.UserDetailsImpl;

//@CrossOrigin(origins = "${cors.allowed-origins}", maxAge = 3600) //Important for CORS
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshJwt = jwtUtils.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

		// String refreshToken = jwtUtils.generateRefreshToken(authentication); // If using refresh tokens.
        return ResponseEntity.ok(new JwtResponse(jwt,
            refreshJwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println("Received signup request: " + signUpRequest); // Print the entire request
    
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
    
        User user = new User(signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
    
        Set<String> strRoles = signUpRequest.getRole();
        System.out.println("Roles from request: " + strRoles); // Print the roles
    
        Set<Role> roles = new HashSet<>();
    
        if (strRoles == null || strRoles.isEmpty()) {
            System.out.println("Roles are null or empty. Assigning ROLE_USER.");
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                System.out.println("Processing role: " + role); // Print each role
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(sellerRole);
                        break;
                    default : //This is redundant now. It is also adding user.
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        break;
                            
                    // NO DEFAULT CASE! //This comment is incorrect now
                }
            });
        }
    
        System.out.println("Final roles to be assigned: " + roles); // Print the final roles
        user.setRoles(roles);
        userRepository.save(user);
    
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
     // Example of a protected endpoint
    // @GetMapping("/user")
    // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Example authorization
    // public String userAccess() {
    //     return "User Content.";
    // }
}