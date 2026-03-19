package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Repositories.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")

public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    //GET all events
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    //GET single event by id
    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    //POST new event
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    //Update new event by id
    @PutMapping("/{eventId}")
    public Event updateEvent(@PathVariable UUID eventId, @RequestBody Event updatedEvent) {
        return eventRepository.findById(eventId)
                .map(event -> {
                    event.setTitle(updatedEvent.getTitle());
                    event.setDescription(updatedEvent.getDescription());
                    event.setStartTime(updatedEvent.getStartTime());
                    event.setEndTime(updatedEvent.getEndTime());
                    event.setLocation(updatedEvent.getLocation());
                    event.setMaxAttendees(updatedEvent.getMaxAttendees());
                    event.setIsPublished(updatedEvent.getIsPublished());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Event>deleteEvent(@PathVariable UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        eventRepository.delete(event);
        return ResponseEntity.ok(event);
    }
}
