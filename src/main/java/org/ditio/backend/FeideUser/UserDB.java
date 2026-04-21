package org.ditio.backend.FeideUser;

import java.util.ArrayList;
import java.util.List;

import org.ditio.backend.Entities.User;
import org.ditio.backend.Enums.UserRole;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserDB extends OidcUserService {

    private final UserRepository userRepository;

    public UserDB(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                        User newUser = new User(feideId, name, email, UserRole.USER);
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