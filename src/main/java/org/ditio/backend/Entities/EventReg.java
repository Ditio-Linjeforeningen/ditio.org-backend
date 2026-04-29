package org.ditio.backend.Entities;

import jakarta.persistence.*;
import org.ditio.backend.Enums.EventRegStatus;

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
    @Column(name = "status")
    private EventRegStatus status;

    @Column(name = "waitlist")
    private int waitlist;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "eventReg", cascade = CascadeType.ALL)
    private RegEventSheet regEventSheet;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public EventReg() {}

    public EventRegId getId() { return id; }
    public void setId(EventRegId id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public EventRegStatus getStatus() { return status; }
    public void setStatus(EventRegStatus status) { this.status = status; }

    public int getWaitlist() { return waitlist; }
    public void setWaitlist(int waitlist) { this.waitlist = waitlist; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public RegEventSheet getRegEventSheet() { return regEventSheet; }
    public void setRegEventSheet(RegEventSheet regEventSheet) { this.regEventSheet = regEventSheet; }
}


































