package org.ditio.backend.FeideUser;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableMethodSecurity
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
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public String getMethodName(@AuthenticationPrincipal OidcUser principal) {
        return "Du er admin";
    }
    
}