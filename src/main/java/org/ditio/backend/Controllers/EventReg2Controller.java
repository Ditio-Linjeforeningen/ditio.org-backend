package org.ditio.backend.Controllers;

import org.ditio.backend.TimeBasedOnetimePassword;
import org.ditio.backend.Entities.EventReg2;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.User;

import org.ditio.backend.Enums.Attendance_Values;
import org.ditio.backend.Repositories.EventReg2Repository;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/EventReg2")

public class EventReg2Controller {

    private final EventReg2Repository repository;
     private static final int STEP_SECONDS = 30;

    private final String secretBase32;
    private final Clock clock = Clock.systemUTC();

    public EventReg2Controller(EventReg2Repository repository, 
        @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
        @Value("${otp.secret.isBase32:OTP_CONFIG_STATUS}") boolean isBase32) {
        this.repository = repository;
         this.secretBase32 = isBase32
                ? secretConfig
                : TimeBasedOnetimePassword.encodeBase32(secretConfig);
    }

    // GET all items
    @GetMapping
    public List<EventReg2> getAllTestAtts() {
        return repository.findAll();
    }

    // GET single item by id
    @GetMapping("/{id}")
    public EventReg2 getTestAtts(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestAtt not found"));
    }

    // POST new item
    @PostMapping
    public EventReg2 createTest(@RequestBody EventReg2 testAtt) {
        return repository.save(testAtt);
        }


    @PutMapping
    public EventReg2 editTestAtt(@PathVariable UUID id){
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
        public ResponseEntity<EventReg2>deleteItem(@PathVariable UUID id){
           EventReg2 testAtt = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
           repository.delete(testAtt);
           return ResponseEntity.ok(testAtt);
        
        }

//OTP
    //Sjekker om inputkode er lik true og endrer oppmøtestatus fra false til true. 
    //https://spring.io/guides/tutorials/rest

    // En enkel DTO som matcher forventet body
public record VerifyRequestDTO(String code) {}


public int test(int a){
return a;
}

@PutMapping("/Attended/{id}")
public ResponseEntity<?> user_attendance_reg(@RequestBody VerifyRequestDTO body, @PathVariable("id") UUID id) {

    String input = body.code().trim();
    boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);

    Attendance_Values neededValue = Attendance_Values.confirmed;

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

                    else{
                    reg.setAttStatus(Attendance_Values.attended); 
                    var saved = repository.save(reg);
                    return ResponseEntity.ok(Map.of(
                            "user_id", saved.getUserId(),
                            "event_id", saved.getEventId(),
                            "att_status", saved.getAttStatus()
                    ));
                    }
                    
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "EventReg2 ikke funnet"))); 
    }
    }
    }
//PUT(KODE): curl -i --request PUT --json 
// "{\"code\":\"1652\",\"att_status\":true}" 
// "http://localhost:8080/testAtt/verify2/879f6b7a-c90f-49d7-b2a9-e6b3154af817"


//curl --json "{\"event_id\": \"123\", \"userIdString\": "OlaNor123", \"att_status\": \"false\"}" http://localhost:8080/testAtt

/*@PutMapping("/verify2/{id}")
    Optional<EventReg2> updateTestAtt(@RequestBody Map<String, Object>body, @PathVariable("id") UUID id) {
        
        String input = String.valueOf(body.getOrDefault("code", "")).trim();

        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);
        if(valid == true /* && deadline*/ /*){
            System.out.print("Input og kode matcher");

            return repository.findById(id)
            .map(testAtt-> {
                testAtt.setAttStatus(Attendance_Values.attended);
                return repository.save(testAtt);
                
            });
        }
        else{
            //System.out.print("Input og kode matcher IKKE");
            return null;
        }
    }*/ 