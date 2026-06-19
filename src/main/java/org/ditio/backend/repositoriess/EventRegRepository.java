package org.ditio.backend.repositoriess;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.ditio.backend.entitiess.enumss.Attendance_Values;
import org.ditio.backend.entitiess.EventReg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegRepository extends JpaRepository<EventReg, UUID> {
 List<EventReg> findByDeadlineBeforeAndAttStatusNot(LocalDateTime now, Attendance_Values status);
 List <EventReg> findAllByAttStatusNot (Attendance_Values status);
}
