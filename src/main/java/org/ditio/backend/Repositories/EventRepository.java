package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Enums.EventRegStatus;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}

