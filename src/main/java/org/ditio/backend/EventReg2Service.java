package org.ditio.backend;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.EventReg2;
import org.ditio.backend.Repositories.EventRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EventReg2Service {

    private final EventRepository eventRepository;

    public EventReg2Service(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Kall denne før du lagrer EventReg2
    public void applyDeadline(EventReg2 reg) {
        UUID eventId = reg.getEventId();
        if (eventId == null) {
            throw new IllegalArgumentException("event_id må settes på EventReg2 før deadline kan beregnes");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event ikke funnet: " + eventId));

        if (event.getStartTime() == null) {
            throw new IllegalStateException("Event.startTime er null for event: " + eventId);
        }

        LocalDateTime deadline = event.getStartTime().toLocalDate().atTime(23, 59, 59);
        reg.setDeadline(deadline);
    }
    
public void add_quarantine_until(EventReg2 reg) {
     UUID eventId = reg.getEventId();
        if (eventId == null) {
            throw new IllegalArgumentException("event_id må settes på EventReg2 før deadline kan beregnes");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event ikke funnet: " + eventId));

        if (event.getStartTime() == null) {
            throw new IllegalStateException("Event.startTime er null for event: " + eventId);
        }

        LocalDateTime quarantine_end_set_to_null = null;
        reg.setQuarantine_end(quarantine_end_set_to_null);
    
    
}
}
