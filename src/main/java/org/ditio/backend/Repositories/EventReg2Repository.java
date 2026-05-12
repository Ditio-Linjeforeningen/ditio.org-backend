package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.EventReg2;
import org.ditio.backend.Enums.Attendance_Values;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventReg2Repository extends JpaRepository<EventReg2, UUID> {
 List<EventReg2> find_All_Not_Attended_Event(LocalDateTime now, Attendance_Values status);

}
