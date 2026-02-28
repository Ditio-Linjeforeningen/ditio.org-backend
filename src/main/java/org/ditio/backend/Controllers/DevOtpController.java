package org.ditio.backend.Controllers;

import org.ditio.backend.TimeBasedOnetimePassword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/dev/otp")
@Profile("dev") // kun aktiv i dev
public class DevOtpController {

    private static final int STEP_SECONDS = 30;

    private final String secretBase32;
    private final Clock clock = Clock.systemUTC();

    public DevOtpController(
            @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
            @Value("${otp.secret.isBase32:OTP_CONFIG_STATUS}") boolean isBase32
    ) {
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

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestBody Map<String, String> body) {
        String input = body.getOrDefault("code", "").trim();
        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, input);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}