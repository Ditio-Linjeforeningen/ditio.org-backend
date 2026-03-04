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
                .requestMatchers("/test").authenticated() // Require login for /test
                .anyRequest().permitAll()  // allow all endpoints
            )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/test", true) // Enable OAuth2 Login
           .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/callback") 
            )
            .defaultSuccessUrl("/test", true)
        );

        return http.build();
    }
}