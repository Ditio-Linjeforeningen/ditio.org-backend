package org.ditio.backend;

import org.ditio.backend.TimeBasedOnetimePassword;
import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.EventReg2;
import org.ditio.backend.Enums.Attendance_Values;
import org.ditio.backend.Repositories.EventReg2Repository;
import org.ditio.backend.Repositories.EventRepository;
import org.ditio.backend.Repositories.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

import java.net.http.HttpResponse;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
public class EventReg3Service {

    private static final int STEP_SECONDS = 30;
    private final Clock clock = Clock.systemUTC();

    private final EventReg2Repository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final String secretBase32;

    public EventReg3Service(
            EventReg2Repository repository,
            UserRepository userRepository,
            EventRepository eventRepository,
            // gjør Clock injiserbar for enklere testing
            @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
            @Value("${otp.secret.isBase32:false}") boolean isBase32
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.secretBase32 = isBase32 ? secretConfig : TimeBasedOnetimePassword.encodeBase32(secretConfig);
    }

    
    public List<EventReg2> findAll() {
        return repository.findAll();
    }

    public EventReg2 findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "EventReg2 not found"));
    }
    
    public void applyDeadline(EventReg2 reg) {

    UUID eventId = reg.getEventId();
    if (eventId == null) {
        throw new IllegalArgumentException("event_id må settes på EventReg2 før deadline kan beregnes");
    }

    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event ikke funnet: " + eventId));

    LocalDateTime start = event.getStartTime();
    if (start == null) {
        throw new IllegalStateException("Event.startTime er null for event: " + eventId);
    }

    // Velg regel:
    // 1) Deadline ved slutten av samme dag som eventets start:
    LocalDateTime deadline = start.toLocalDate().atTime(23, 59, 59);

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

    // Blokker registrering hvis bruker er i karantene
    private void blockIfUserInQuarantine(EventReg2 reg) {
        if (reg.getUserId() == null) return;

        // Eksempel: hent bruker og sjekk karantene til (tilpass etter din User-modell)
        var userOpt = userRepository.findById(reg.getUserId());
        userOpt.ifPresent(user -> {
            var quarantineEnd = user.getQuarantine_until(); // eller tilsvarende felt
            if (quarantineEnd != null && quarantineEnd.isAfter(LocalDateTime.now(clock))) {
                throw new ResponseStatusException(FORBIDDEN, "Bruker er i karantene til " + quarantineEnd);
            }
        });
    }

    @Transactional
    public EventReg2 create(EventReg2 reg) {
        // 1) Valider domene-regler
        applyDeadline(reg);
        add_quarantine_until(reg);
        blockIfUserInQuarantine(reg);

        // 2) Initier status hvis nødvendig
        if (reg.getAttStatus() == null) {
            reg.setAttStatus(Attendance_Values.confirmed);
        }

        return repository.save(reg);
    }

    // Merk som no_show hvis fristen er passert og status ikke er attended/waitlist
    @Transactional
    public EventReg2 markNoShow(UUID id) {
        var reg = findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reg.getDeadline() == null) {
            throw new ResponseStatusException(CONFLICT, "Registreringen mangler deadline");
        }

        if (now.isAfter(reg.getDeadline())
                && reg.getAttStatus() != Attendance_Values.attended
                && reg.getAttStatus() != Attendance_Values.waitlist) {

            reg.setAttStatus(Attendance_Values.no_show);
            // Oppgi karantene som 30 dager etter deadline
            reg.setQuarantine_end(reg.getDeadline().plusDays(30));
            return repository.save(reg);
        }

        // Ingen endring å gjøre
        return reg;
    }
    
    // DTO som kan serialiseres direkte av Spring
    public record OtpResponse(String code, Instant expiresAt) {}

    public OtpResponse currentOtp() {
        long now = Instant.now(clock).getEpochSecond();
        String code = TimeBasedOnetimePassword.generateTOTP(secretBase32);
        long remaining = STEP_SECONDS - (now % STEP_SECONDS);
        Instant expiresAt = Instant.ofEpochSecond(now + remaining);
        return new OtpResponse(code, expiresAt);
    }

    
    /*boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);

    Attendance_Values neededValue = Attendance_Values.confirmed;
    LocalDateTime TimeNow = LocalDateTime.now();*/


   /* public boolean validateOtp(String input) {

        if (input == null) return false;
        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input.trim());
        return valid;
    }*/

    // Bekreft oppmøte med OTP
    @Transactional
    public EventReg2 confirmAttendance(UUID id, String otpInput) {

        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, otpInput);

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feil kode");
        }

        var reg = findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reg.getAttStatus() != Attendance_Values.confirmed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Du er ikke registrert til arrangementet, og kan ikke melde oppmøte.");
        }

        if (reg.getDeadline() != null && now.isAfter(reg.getDeadline())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Du møtte ikke opp på arrangementet, og kan ikke melde oppmøte.");
        }

        else{
            reg.setAttStatus(Attendance_Values.attended);
            reg.setQuarantine_end(null);
            return repository.save(reg);
        }
        
    }


    @Transactional
    public EventReg2 update(EventReg2 reg) {
        // Legg inn ønskede valideringer og regelverk
        return repository.save(reg);
    }

    @Transactional
    public EventReg2 delete(UUID id) {
        var reg = findById(id);
        repository.delete(reg);
        return reg;
    }
}