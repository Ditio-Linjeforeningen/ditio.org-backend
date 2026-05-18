package org.ditio.backend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.ditio.backend.Enums.Attendance_Values;

@Entity
@Table(name = "event_reg2")
public class EventReg2 {

    @Id
    @GeneratedValue
    @Column(name = "event_reg_id", columnDefinition = "uuid")
    private UUID event_reg_id;

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

    @JsonProperty("deadline")
    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @JsonProperty("quarantine_end")
    @Column(name = "quarantine_end", nullable = true)
    private LocalDateTime quarantine_end;


    // Read-only relasjoner (lastes ved behov, men brukes ikke til å skrive kolonnene)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    

    public EventReg2() {}

    // getters/setters
    public UUID getEventRegId() { return event_reg_id; }
    public void setEventRegId(UUID event_reg_id) { this.event_reg_id = event_reg_id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public Attendance_Values getAttStatus() { return attStatus; }
    public void setAttStatus(Attendance_Values attStatus) { this.attStatus = attStatus; }
    
    public LocalDateTime getDeadline() {return deadline;}
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline;}

    public LocalDateTime getQuarantine_end(){return quarantine_end;}
    public void setQuarantine_end(LocalDateTime quarantine_end) 
    {this.quarantine_end = quarantine_end;}

    /*public User getUser() { return user; }
    public Event getEvent() { return event; }*/

}