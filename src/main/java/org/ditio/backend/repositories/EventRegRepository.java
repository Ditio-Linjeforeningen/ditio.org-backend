package org.ditio.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.ditio.backend.entities.enumss.Attendance_Values;
import org.ditio.backend.entities.EventReg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegRepository extends JpaRepository<EventReg, UUID> {
 List<EventReg> findByDeadlineBeforeAndAttStatusNot(LocalDateTime now, Attendance_Values status);
 List <EventReg> findAllByAttStatusNot (Attendance_Values status);
}
