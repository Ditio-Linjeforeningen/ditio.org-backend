package org.ditio.backend;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String login() {
        return "<html><body><h1>Login</h1>" +
               "<a href='/feide/test'>Login with Feide</a>" +
               "</body></html>";
    }

    @GetMapping("/feide/test")
    public String test(@AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            return "User not authenticated - something went wrong";
        }
        return "Gratulerer! Du er logget inn som: " + user.getFullName() + " (" + user.getEmail() + ")";
    }
}