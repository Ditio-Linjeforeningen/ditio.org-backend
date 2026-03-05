package org.ditio.backend;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/feide/test")
    public String test(@AuthenticationPrincipal OidcUser user) {
        return "Gratulerer! Du er logget inn som: " + user.getFullName() + " (" + user.getEmail() + ")";
    }
}