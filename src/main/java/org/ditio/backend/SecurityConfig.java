package org.ditio.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig { 

    // forbindelse tiol UserDB for at kunne synkronisere brugerdata ved login
    private final UserDB userDB;
    
    public SecurityConfig(UserDB userDB) {
        this.userDB = userDB;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/feide/test").authenticated() // må logge ind for at komme /feide/test
                .anyRequest().permitAll()  // tilgængeligt for alle andre endpoints
            )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/feide/test", true) //OAuth2 Login
           .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/callback") 
            )
            .defaultSuccessUrl("/feide/test", true)
        );

        return http.build();
    }
}