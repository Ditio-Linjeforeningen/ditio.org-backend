package org.ditio.backend;

import org.ditio.backend.Entities.User;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
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
        OidcUser oidcUser = super.loadUser(userRequest);
        
        // .getSubject() er et unikt id fra Feide, som kan bruges som primær nøgle
        String feideId = oidcUser.getSubject(); 
        String name = oidcUser.getFullName();
        String email = oidcUser.getEmail();

        // synkroniser til database, enten oppdatere bruger eller lave en ny
        User user = userRepository.findById(feideId)
                .map(existingUser -> {
                    existingUser.setNavn(name);
                    existingUser.setEmail(email);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(new User(feideId, name, email)));

        return oidcUser;
    }
}