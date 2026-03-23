package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.Item;
import org.ditio.backend.Repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")

public class ItemController {

    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    // GET all items
    @GetMapping
    public List<Item> getAllItems() {
        return repository.findAll();
    }

    // GET single item by id
    //Testcommit
    @GetMapping("/{id}")
    public Item getItem(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // POST new item
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return repository.save(item);
    }

//Fungerer:
  @DeleteMapping("/{id}")
        public ResponseEntity<Item>deleteItem(@PathVariable UUID id){
           Item item = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
           repository.delete(item);
           return ResponseEntity.ok(item);
        
        }
}