package org.ditio.backend.Services;

import org.ditio.backend.Entities.*;
import org.ditio.backend.Enums.EventRegStatus;
import org.ditio.backend.Repositories.EventRegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventRegServices {

    @Autowired
    private EventRegRepository eventRegRepository;

    public EventReg registerForEvent(Event event, User user, RegEventSheet sheet) {

        long confirmedCount = eventRegRepository.countByEventAndStatus(
                event, EventRegStatus.CONFIRMED
        );

        EventReg reg = new EventReg();
        reg.setId(new EventRegId(event.getEventId(), user.getFeideId()));
        reg.setEvent(event);
        reg.setUser(user);

        if (confirmedCount < event.getMaxAttendees()) {
            reg.setStatus(EventRegStatus.CONFIRMED);
        } else {
            reg.setStatus(EventRegStatus.WAITLIST);
        }

        // Koble sheet til reg
        if (sheet != null) {
            sheet.setEventReg(reg);
            reg.setRegEventSheet(sheet);
        }

        return eventRegRepository.save(reg);
    }
}



