package org.ditio.backend.Controllers;

import org.ditio.backend.EventReg2Service;
import org.ditio.backend.EventReg3Service;
import org.ditio.backend.TimeBasedOnetimePassword;
import org.ditio.backend.Entities.EventReg2;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.User;

import org.ditio.backend.Enums.Attendance_Values;
import org.ditio.backend.Repositories.EventReg2Repository;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.sql.Time;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/EventReg3")
public class EventReg3Controller {

    private final EventReg3Service service;

    public EventReg3Controller(EventReg3Service service) {
        this.service = service;
    }

    // OTP nåværende kode
    @GetMapping("/current")
    public EventReg3Service.OtpResponse current() {
        return service.currentOtp();
    }

    // GET all
    @GetMapping
    public List<EventReg2> getAll() {
        return service.findAll();
    }

    // GET by id
    @GetMapping("/{id}")
    public EventReg2 getEventReg2(@PathVariable UUID id) {
        return service.findById(id);
    }

    // POST create
    @PostMapping
    public EventReg2 createEventReg2(@RequestBody EventReg2 eventReg2) {
        return service.create(eventReg2);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<EventReg2> deleteEventReg2(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }

    // DTO for OTP input (som i din original)
    public record Verify_Attendance_Code_DTO(String code) {}
    public record AttendanceResponse
    (
        UUID user_id,
        UUID event_id,
        Attendance_Values att_status,
        LocalDateTime deadline,
        LocalDateTime quarantine_end
    ) {}

    // Merk no_show hvis vilkår er oppfylt
    @PutMapping("/Activate_Quarantine_enddate/{id}")
    public ResponseEntity<?> markNoShow(@PathVariable UUID id) {
        var saved = service.markNoShow(id);
        // Hvis ingenting ble endret, kan du velge å returnere 204/200 med nåværende status
        return ResponseEntity.ok(Map.of(
                "user_id", saved.getUserId(),
                "event_id", saved.getEventId(),
                "att_status", saved.getAttStatus(),
                "deadline", saved.getDeadline(),
                "quarantine_until", saved.getQuarantine_end()
        ));
    }

    // Bekreft oppmøte med OTP
    @PutMapping("/Attended/{id}")
    public ResponseEntity<?> user_attendance_reg(@RequestBody Verify_Attendance_Code_DTO body,
                                                 @PathVariable("id") UUID id) {
        
        var saved = service.confirmAttendance(id, body.code());
        
          Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("user_id", saved.getUserId());
            payload.put("event_id", saved.getEventId());
            payload.put("att_status", saved.getAttStatus());
            payload.put("deadline", saved.getDeadline());           // kan være null
            payload.put("quarantine_end", saved.getQuarantine_end()); // er null ved attended
            return ResponseEntity.ok(payload);
        
    }
}