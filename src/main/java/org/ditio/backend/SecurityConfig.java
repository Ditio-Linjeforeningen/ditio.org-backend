package org.ditio.backend;

import org.ditio.backend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // forbindelse tiol UserDB for at kunne synkronisere brugerdata ved login
    private final UserService userDB;
    
    public SecurityConfig(UserService userDB) {
        this.userDB = userDB;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) //slå på i produktion
            .authorizeHttpRequests(auth -> auth
                //Alle kan se events
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/events/**").permitAll()

                //Bare superadmin kan ændre roller
                .requestMatchers("/api/admin/**").hasAnyRole("SUPER_ADMIN")

                // Resten er åbent for alle
                .anyRequest().permitAll()  
            )
                .oauth2Login(oauth2 -> oauth2
                //Synkroniserer med UserDB service
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(userDB))
                //henter token hos feide
                .redirectionEndpoint(redirection -> redirection.baseUri("/login/callback"))
                //Går på default til /api/users/meg efter login
                //Front end burde kunne overstyre denne her, 
                // hvis de vil sende brugeren tilbage hvor den kom fra efter indlogging
                .defaultSuccessUrl("/api/users/meg", true)
        );

        return http.build();
    }
}