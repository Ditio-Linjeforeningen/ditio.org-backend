package org.ditio.backend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.UUID;
import org.ditio.backend.Enums.Attendance_Values;

@Entity
@Table(name = "event_reg2")
public class EventReg2 {

    @Id
    @GeneratedValue
    @Column(name = "event_reg_id", columnDefinition = "uuid")
    private UUID eventRegId;

    // Skrivbare FK-kolonner som kommer fra JSON
    @JsonProperty("user_id")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @JsonProperty("event_id")
    @Column(name = "event_id", nullable = false, columnDefinition = "uuid")
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @JsonProperty("att_status")
    @Column(name = "att_status", nullable = false)
    private Attendance_Values attStatus;

    // Read-only relasjoner (lastes ved behov, men brukes ikke til å skrive kolonnene)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    protected EventReg2() {}

    // getters/setters
    public UUID getEventRegId() { return eventRegId; }
    public void setEventRegId(UUID eventRegId) { this.eventRegId = eventRegId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public Attendance_Values getAttStatus() { return attStatus; }
    public void setAttStatus(Attendance_Values attStatus) { this.attStatus = attStatus; }

    public User getUser() { return user; }
    public Event getEvent() { return event; }
}