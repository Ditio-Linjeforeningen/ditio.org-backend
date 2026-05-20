package org.ditio.backend.Repositories;

import java.util.UUID;

import org.ditio.backend.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
