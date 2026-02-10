package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.Item;
import org.ditio.backend.Repositories.ItemRepository;
import org.springframework.web.bind.annotation.*;

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
}