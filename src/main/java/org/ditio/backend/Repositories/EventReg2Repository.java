package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.EventReg2;
import org.ditio.backend.Enums.Attendance_Values;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventReg2Repository extends JpaRepository<EventReg2, UUID> {
    //Navnet må inntil videre være sånn pga navneregler i repo. Prøvde å fikse, men fikk ikke kjørt programmet. 
 List<EventReg2> findByDeadlineBeforeAndAttStatusNot(LocalDateTime now, Attendance_Values status);
 List <EventReg2> findAllByAttStatusNot (Attendance_Values status, Attendance_Values waitlist);
 List <EventReg2> findAllByAttStatusBothNot (Attendance_Values status);

}
