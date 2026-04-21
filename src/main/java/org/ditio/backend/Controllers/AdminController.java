package org.ditio.backend.Controllers;

import java.util.Map;

import org.ditio.backend.Enums.UserRole;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//kontroller for superadmin til at gi og ta admin rettigheder

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')") 
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //giver admin rettigheder, via oslomet epost
    @PostMapping("/promote")
    public ResponseEntity<String> promoteToAdmin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        // Retter til findByEmail for at matche UserRepository
        return userRepository.findByEmail(email)
            .map(user -> {
                user.setRole(UserRole.ADMIN);
                userRepository.save(user);
                return ResponseEntity.ok("Brugeren " + email + " er nu admin.");
            })
            .orElse(ResponseEntity.notFound().build());
    }

    //Fjerne admin rettigheder via oslomet epost
    @PostMapping("/demote")
    public ResponseEntity<String> demoteToUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        return userRepository.findByEmail(email)
            .map(user -> {
                // Kan ikke ændre på rettigheder til en superadmin
                if (user.getRole() == UserRole.SUPER_ADMIN) {
                    return ResponseEntity.badRequest().body("Du kan ikke fjerne rettigheder fra en SUPER_ADMIN.");
                }
                
                user.setRole(UserRole.USER);
                userRepository.save(user);
                return ResponseEntity.ok("Brugeren " + email + " er nu almindelig bruker.");
            })
            .orElse(ResponseEntity.notFound().build());
    }
}