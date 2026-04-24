package org.ditio.backend.Controllers;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableMethodSecurity
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        //SLET HTML, DETTE ER BARE FOR TESTING
        return "Ditio API kører!" + 
               
               "<html><body>" +
               "<a href='/oauth2/authorization/feide'>Login</a>" +
               "</body></html>";
    }
}