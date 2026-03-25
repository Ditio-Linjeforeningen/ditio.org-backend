package org.ditio.backend.Controllers;

import java.util.List;
import java.util.Optional;

import org.ditio.backend.Entities.User;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // find bruger baseret på ID
    // MÅ BESKYTTES BAK ADMIN LOGIN
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id); 
    }

    // finder alle brugere som er registret i databasen
    // MÅ BESKYTTES BAK ADMIN LOGIN
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}