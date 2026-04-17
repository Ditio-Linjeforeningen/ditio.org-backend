package org.ditio.backend.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
    @Table(name = "event_reg")
    public class EventReg {

    @EmbeddedId
    private EventRegId id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @MapsId("feideId")
    @JoinColumn(name = "feide_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")  //confirmed, waitlist, attended, no-show)
    private EventRegStatus status = EventRegStatus.EVENTREG;

    @Column(name = "waitlist")
    private int waitlist;


    public EventReg() {
    }
}




































