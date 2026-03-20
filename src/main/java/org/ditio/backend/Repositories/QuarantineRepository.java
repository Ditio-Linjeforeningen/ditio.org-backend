package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.Quarantine;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface QuarantineRepository extends JpaRepository<Quarantine, UUID> {
}