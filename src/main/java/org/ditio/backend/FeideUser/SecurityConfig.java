package org.ditio.backend.FeideUser;

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
    private final UserDB userDB;
    
    public SecurityConfig(UserDB userDB) {
        this.userDB = userDB;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                //Alle kan se events
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/events/**").permitAll()
                
                // Event CRUD kræver admin rettigheder
                .requestMatchers("/events/**").hasAnyRole("ADMIN", "SUPER_ADMIN")

                // Kun for test 
                .requestMatchers("/feide/test").authenticated()

                //Bare superadmin kan ændre roller
                .requestMatchers("/api/admin/**").hasAnyRole("SUPER_ADMIN")

                // Resten er åbent for alle
                .anyRequest().permitAll()  
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