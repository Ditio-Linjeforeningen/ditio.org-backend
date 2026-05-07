package org.ditio.backend.Controllers;

import org.ditio.backend.EventReg2Service;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/EventReg3")


public class EventReg3Controller {
    @Autowired
    private EventReg2Service eventReg2Service;    // den lille servicen som kun setter deadline

    private final EventReg2Repository repository;
    
     private static final int STEP_SECONDS = 30;

    private final String secretBase32;
    private final Clock clock = Clock.systemUTC();


    public EventReg3Controller(EventReg2Repository repository, 
        EventReg2Service eventReg2Service,
        @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
        @Value("${otp.secret.isBase32:OTP_CONFIG_STATUS}") boolean isBase32) {
        this.repository = repository;
         this.secretBase32 = isBase32
                ? secretConfig
                : TimeBasedOnetimePassword.encodeBase32(secretConfig);
    }

    @GetMapping("/current")
    public Map<String, Object> current() {
        long now = Instant.now(clock).getEpochSecond();
        String code = TimeBasedOnetimePassword.generateTOTP(secretBase32);

        long remaining = STEP_SECONDS - (now % STEP_SECONDS);
        Instant expiresAt = Instant.ofEpochSecond(now + remaining);

        return Map.of("code", code, "expiresAt", expiresAt.toString());
    }

    // GET all items
    @GetMapping
    public List<EventReg2> getAllTestAtts() {
        return repository.findAll();
    }

    // GET single item by id
    @GetMapping("/{id}")
    public EventReg2 getEventReg2(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventReg2 not found"));
    }

    // POST new eventreg2
    @PostMapping
    //NEED TO BLOCK OUT PEOPLE IN QUARANTINE 
    public EventReg2 createEventReg2(@RequestBody EventReg2 eventReg2) {
        
        eventReg2Service.applyDeadline(eventReg2);
        eventReg2Service.add_quarantine_until(eventReg2);
        return repository.save(eventReg2);
        
    }

    /*@PostMapping
    //Service testing 
    public EventReg2 addEventReg2(@RequestBody EventReg2 eventReg2) {
        return EventReg2Service.saveEventReg2(eventReg2);
        
    }*/

    @PutMapping
    public EventReg2 editEventReg2(@PathVariable UUID id){
        return null;
        
    }

    @PutMapping("/Register_Att_Status/{id}")
    public boolean editStatus_TestAtt(@PathVariable UUID id, @RequestBody boolean att_status,  @RequestParam("isEnabled") boolean newStatus, Model model){
        if(newStatus == true){
            att_status = newStatus;
            return att_status;
        }
        else {
            return att_status;
        }
        
        
    }//https://education.launchcode.org/java-web-dev-curriculum/controllers-and-routing/reading/controllers-with-parameters/index.html
//https://javabulletin.substack.com/p/6-ways-to-pass-parameters-to-spring
//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/support/RedirectAttributes.html
//https://stackoverflow.com/questions/14470111/spring-redirectattributes-addattribute-vs-addflashattribute

  @DeleteMapping("/{id}")
        public ResponseEntity<EventReg2>deleteEventReg2(@PathVariable UUID id){
           EventReg2 eventReg2 = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventReg2 not found"));
           repository.delete(eventReg2);
           return ResponseEntity.ok(eventReg2);
        
        }

        
//OTP
    //Sjekker om inputkode er lik true og endrer oppmøtestatus fra false til true. 
    //https://spring.io/guides/tutorials/rest

    // En enkel DTO som matcher forventet body
public record Verify_Attendance_Code_DTO(String code) {}


@PutMapping("/Q/{id}")
public ResponseEntity<?> markNoShow(@PathVariable UUID id) {
    return repository.findById(id)
        .map(reg -> {
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(reg.getDeadline())
                && (reg.getAttStatus() != Attendance_Values.attended
                    && reg.getAttStatus() != Attendance_Values.waitlist)) {
                        
                        reg.setAttStatus(Attendance_Values.no_show);
                        reg.setQuarantine_end(reg.getDeadline().plusDays(30));
                        var saved = repository.save(reg);
                        return ResponseEntity.ok(Map.of(
                         "user_id", saved.getUserId(),
                         "event_id", saved.getEventId(),
                         "att_status", saved.getAttStatus(),
                         "deadline", saved.getDeadline(),
                         "quarantine_until", saved.getQuarantine_end()
                        ));
                    } else {
                // Fristen er ikke passert – ingen endring
                return null;
            }
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", "EventReg2 ikke funnet")));
    }
    


@PutMapping("/Attended/{id}")
public ResponseEntity<?> user_attendance_reg(@RequestBody Verify_Attendance_Code_DTO body, @PathVariable("id") UUID id) {

    String input = body.code().trim();
    boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);

    Attendance_Values neededValue = Attendance_Values.confirmed;
    LocalDateTime TimeNow = LocalDateTime.now();


    if (!valid) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Feil kode"));
    }

    else{
        return repository.findById(id)
                .map(reg -> {

                    if (reg.getAttStatus() != neededValue){
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Du er ikke registrert til arrrangementet, og kan ikke melde oppmøte."));
                    }

                    if (TimeNow.isAfter(reg.getDeadline())){
                         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Du møtte ikke opp på arrangementet, og kan ikke melde oppmøte."));
                    }


                    else{
                        reg.setAttStatus(Attendance_Values.attended); 
                        reg.setQuarantine_end(null);
                        var saved = repository.save(reg);
                        return ResponseEntity.ok(Map.of(
                            "user_id", saved.getUserId(),
                            "event_id", saved.getEventId(),
                            "att_status", saved.getAttStatus(),
                            "deadline", saved.getDeadline(),
                            "quarantine_end", saved.getQuarantine_end()
                         ));
                        }
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "EventReg2 ikke funnet"))); 
                    }
                }
                }