package org.ditio.backend.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.ditio.backend.Entities.Enums.Attendance_Values;
import org.ditio.backend.Entities.EventReg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegRepository extends JpaRepository<EventReg, UUID> {
    //Navnet må inntil videre være sånn pga navneregler i repo. Prøvde å fikse, men fikk ikke kjørt programmet. 
 List<EventReg> findByDeadlineBeforeAndAttStatusNot(LocalDateTime now, Attendance_Values status);
 List <EventReg> findAllByAttStatusNot (Attendance_Values status);
 
 //List <EventReg2> findAllByAttStatusBothNot (Attendance_Values status);

}
