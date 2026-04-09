// QuarantineController.java
/*package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.Quarantine;
import org.ditio.backend.Service.QuarantineService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quarantine")
public class QuarantineController {

    private final QuarantineService quarantineService;

    public QuarantineController(QuarantineService quarantineService) {
        this.quarantineService = quarantineService;
    }

    @GetMapping
    public List<Quarantine> getAllQuarantines() {
        return quarantineService.getAll();
    }

    @GetMapping("/{id}")
    public Quarantine getQuarantine(@PathVariable UUID id) {
        return quarantineService.getById(id);
    }

    // Opprett og koble karantene til bruker
    @PostMapping
    public ResponseEntity<Void> createQuarantine(
            @RequestParam("feideId") String feideId,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate quarantineEnd) {

        quarantineService.startQuarantine(feideId, quarantineEnd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Avslutt/slett karantene ved quarantineId (finner user og nuller FK)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuarantine(@PathVariable UUID id) {
        quarantineService.endQuarantineByQuarantineId(id);
        return ResponseEntity.noContent().build();
    }

    // Alternativt: avslutt for bruker
    @PatchMapping("/user/{feideId}/end")
    public ResponseEntity<Void> endForUser(@PathVariable String feideId) {
        quarantineService.endQuarantineForUser(feideId);
        return ResponseEntity.noContent().build();
    }
}*/

/* DENNE BRUKES
package org.ditio.backend.Controllers;
import org.ditio.backend.Entities.Quarantine;
//import org.ditio.backend.Entities.User;
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
                .orElseThrow(() -> new RuntimeException("Quarantine not found"));
    }

    // POST new item
    //If Att_status = false og date = passed (activate this method) with a trigger
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
*/