package org.ditio.backend.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ditio.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    //Optional metode for at finde bruge via epost
    Optional<User> findByEmail(String email);
    public Object getUserByEmail(String email);
    List <User> findAllByQuarantineUntilNot (LocalDateTime quarantine_end);
    //List<User> findAllByQuarantineUntilIsNotNull();
    List<User> findAllByQuarantineUntilBefore(LocalDateTime quarantine_end);
   
}