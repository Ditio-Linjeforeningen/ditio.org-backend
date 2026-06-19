package org.ditio.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.ditio.backend.entities.User;
import org.ditio.backend.entities.enumss.UserRole;
import org.ditio.backend.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService extends OidcUserService{

    private final UserRepository userRepository;
    private final EventRegService eventRegService;

    public UserService(UserRepository userRepository, EventRegService eventRegService) {
        this.userRepository = userRepository;
        this.eventRegService = eventRegService;
    }

    @Override   
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // 1.Henter info fra Feide-token
        OidcUser oidcUser = super.loadUser(userRequest);
        
        try {
            String feideId = oidcUser.getSubject();
            String name = oidcUser.getFullName();
            String email = oidcUser.getEmail();

            // 2. Opdater eksisternde eller opretter ny bruger i databasen
            User user = userRepository.findById(feideId)
                    .map(existingUser -> {
                        existingUser.setNavn(name);
                        existingUser.setEmail(email);
                        // Beholder den eksisterende rolle fra databasen
                        return userRepository.save(existingUser);
                    })
                    .orElseGet(() -> {
                        User newUser = new User(feideId, name, email, UserRole.USER, null);
                        return userRepository.save(newUser);
                    });

            // 3. Bruger Spring Security Authorities for RBAC
            List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            // 4. Returner bruger
            return new DefaultOidcUser(
                    authorities, 
                    oidcUser.getIdToken(), 
                    oidcUser.getUserInfo()
            );

        } catch (Exception e) {
            //feilhåndtering og logging
            System.err.println(e.getMessage());
            return oidcUser;
        }
    }
}