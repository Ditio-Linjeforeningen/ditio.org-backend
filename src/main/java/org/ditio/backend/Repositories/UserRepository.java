package org.ditio.backend.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.ditio.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    //Optional<User> findByQuarantine_Quarantine_id(UUID quarantineId);
}