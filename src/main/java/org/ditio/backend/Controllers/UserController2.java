package org.ditio.backend.Controllers;


import org.ditio.backend.Service.QuarantineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController2 {
    private final QuarantineService quarantineService;

    public UserController2(QuarantineService quarantineService) {
        this.quarantineService = quarantineService;
    }

    @PatchMapping("/{feideId}/quarantine/end")
    public ResponseEntity<Void> endQuarantine(@PathVariable String feideId) {
        quarantineService.endQuarantineForUser(feideId);
        return ResponseEntity.noContent().build();
    }
}