package org.ditio.backend.repositories;

import java.util.UUID;

import org.ditio.backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
