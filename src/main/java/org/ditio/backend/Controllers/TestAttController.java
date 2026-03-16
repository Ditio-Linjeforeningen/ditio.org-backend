
package org.ditio.backend.Controllers;

import org.ditio.backend.TimeBasedOnetimePassword;
import org.ditio.backend.Entities.TestAtt;
import org.ditio.backend.Repositories.TestAttRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/testAtt")

public class TestAttController {

    private final TestAttRepository repository;
     private static final int STEP_SECONDS = 30;

    private final String secretBase32;
    private final Clock clock = Clock.systemUTC();

    public TestAttController(TestAttRepository repository, 
        @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
        @Value("${otp.secret.isBase32:OTP_CONFIG_STATUS}") boolean isBase32) {
        this.repository = repository;
         this.secretBase32 = isBase32
                ? secretConfig
                : TimeBasedOnetimePassword.encodeBase32(secretConfig);
    }

    // GET all items
    @GetMapping
    public List<TestAtt> getAllTestAtts() {
        return repository.findAll();
    }

    // GET single item by id
    @GetMapping("/{id}")
    public TestAtt getTestAtts(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestAtt not found"));
    }

    // POST new item
    @PostMapping
    public TestAtt createTest(@RequestBody TestAtt testAtt) {
        return repository.save(testAtt);
        }

    @PutMapping
    public TestAtt editTestAtt(@PathVariable UUID id){
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
        public ResponseEntity<TestAtt>deleteItem(@PathVariable UUID id){
           TestAtt testAtt = repository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
           repository.delete(testAtt);
           return ResponseEntity.ok(testAtt);
        
        }

//OTP
@GetMapping("/current")
    public Map<String, Object> current() {
        long now = Instant.now(clock).getEpochSecond();
        String code = TimeBasedOnetimePassword.generateTOTP(secretBase32);

        long remaining = STEP_SECONDS - (now % STEP_SECONDS);
        Instant expiresAt = Instant.ofEpochSecond(now + remaining);

        return Map.of("code", code, "expiresAt", expiresAt.toString());
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestBody Map<String, String> body) {
        String input = body.getOrDefault("code", "").trim();
        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);
        return ResponseEntity.ok(Map.of("true", valid));
    }

    //Forsøk på å kombinere OTP validering og PUT endring av status 
    //https://spring.io/guides/tutorials/rest
    @PutMapping("/verify2/{id}")
    Optional<TestAtt> updateTestAtt(@RequestBody TestAtt testAtta, @RequestBody Map<String, String>body, @PathVariable UUID id) {
        String input = body.getOrDefault("code", "").trim();

        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);
        if(valid == true){
            System.out.print("Input og kode matcher");

            return repository.findById(id)
            .map(testAtt-> {
                testAtt.setAtt_status(testAtta.getAtt_status());
                return repository.save(testAtt);
            });
        }
        else{
            //System.out.print("Input og kode matcher IKKE");
            return null;
        }
    }

}