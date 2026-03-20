package org.ditio.backend.Controllers;
import org.ditio.backend.Entities.Quarantine;
import org.ditio.backend.Entities.User;
import org.ditio.backend.Repositories.QuarantineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quarantine")


public class QuarantineController {

    private final QuarantineRepository repository;

    public QuarantineController (QuarantineRepository repository) {
        this.repository = repository;
    }

    // GET all items
    @GetMapping
    public List<Quarantine> getAllQurantines() {
        return repository.findAll();
    }

    // GET single item by id
    //Testcommit
    @GetMapping("/{id}")
    public Quarantine getQuarantine(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // POST new item
    //If Att_status = false og date = passed (activate this method)
    @PostMapping
    public Quarantine createQuarantine(@RequestBody Quarantine quarantine) {
        return repository.save(quarantine);
    }

  @DeleteMapping("/{id}")
        public ResponseEntity<Quarantine>deleteItem(@PathVariable UUID id){
           Quarantine quarantine = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "quarantine not found"));
           repository.delete(quarantine);
           return ResponseEntity.ok(quarantine);
        
        }

     
}
