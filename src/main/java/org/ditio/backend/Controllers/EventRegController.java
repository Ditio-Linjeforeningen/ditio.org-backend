package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.*;
import org.ditio.backend.Enums.FoodPreference;
import org.ditio.backend.Services.EventRegServices;
import org.ditio.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventRegController {

    @Autowired
    private EventRegServices eventRegServices;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{eventId}/register/{userId}")
    public ResponseEntity<?> registerForEvent(
            @PathVariable UUID eventId,
            @PathVariable String userId,
            @RequestBody(required = false) RegEventSheet sheet
    ) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event ikke funnet"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Bruker ikke funnet"));

        EventReg saved = eventRegServices.registerForEvent(event, user, sheet);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}