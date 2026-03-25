package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.User;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")

public class UserController2 {

    private final UserRepository repository;

    public UserController2(UserRepository repository) {
        this.repository = repository;
    }

    // GET all items
    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // GET single item by id
    //Testcommit
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // POST new item
    @PostMapping
    public User createItem(@RequestBody User item) {
        return repository.save(item);
    }

//Fungerer:
  @DeleteMapping("/{id}")
        public ResponseEntity<User>deleteItem(@PathVariable String id){
           User user = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
           repository.delete(user);
           return ResponseEntity.ok(user);
        
        }
}