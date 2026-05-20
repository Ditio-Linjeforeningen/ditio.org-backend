package org.ditio.backend.Controllers;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ditio.backend.Entities.EventReg;
import org.ditio.backend.Services.EventRegService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/EventReg")
public class EventRegController {

    private final EventRegService service;

    public EventRegController(EventRegService eventRegService) {
        this.service = eventRegService;
    }

    // OTP nåværende kode
    @GetMapping("/current")
    public EventRegService.OtpResponse current() {
        return service.currentOtp();
    }

    // GET all
    @GetMapping
    public List<EventReg> getAll() {
        return service.findAll();
    }

    // GET by id
    @GetMapping("/{id}")
    public EventReg getEventReg(@PathVariable UUID id) {
        return service.findById(id);
    }

    // POST create
    @PostMapping
    public EventReg createEventReg(@RequestBody EventReg eventReg) {
        return service.create(eventReg);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<EventReg> deleteEventReg(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @DeleteMapping("/Delete_All_eventRegs")
    public ResponseEntity<List<EventReg>> deleteAllEventRegs(){
        return ResponseEntity.ok(service.deleteAll());
    }

    // DTO for OTP input (som i din original)
    public record Verify_Attendance_Code_DTO(String code) {}
    
    //There is an automatic one as well in the service:
    @PutMapping("/Manual_quarantine/{id}")
    public ResponseEntity<?> Manually_mark_student_as_quarantined(@PathVariable UUID id) {

        var saved = service.Manually_mark_student_as_quarantined_if_noshow(id);
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