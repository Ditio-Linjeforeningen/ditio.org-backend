package org.ditio.backend.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
    @Table(name = "event_reg2")
    public class EventReg2 {

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @MapsId("feideId")
    @JoinColumn(name = "feide_id")
    private User user;

}