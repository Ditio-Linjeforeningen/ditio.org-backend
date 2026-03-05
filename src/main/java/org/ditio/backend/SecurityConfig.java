package org.ditio.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/feide/test").authenticated() // Require login for /feide/test
                .anyRequest().permitAll()  // allow all endpoints
            )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/feide/test", true) // Enable OAuth2 Login
           .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/callback") 
            )
            .defaultSuccessUrl("/feide/test", true)
        );

        return http.build();
    }
}