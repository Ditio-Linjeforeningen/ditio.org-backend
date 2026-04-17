package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.EventReg;
import org.ditio.backend.Entities.EventRegId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRegRepository  extends JpaRepository<EventReg, EventRegId>{

}


