
package org.ditio.backend.Controllers;

import org.ditio.backend.Entities.TestAtt;
import org.ditio.backend.Repositories.TestAttRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/testAtt")

public class TestAttController {

    private final TestAttRepository repository;

    public TestAttController(TestAttRepository repository) {
        this.repository = repository;
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

}