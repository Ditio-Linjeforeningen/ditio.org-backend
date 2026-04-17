package org.ditio.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EventRegId implements Serializable {
    @Column(name = "event_id")
    private UUID eventId;

    @Column(name= "feide_id")
    private String feideId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EventRegId() {}

    public EventRegId(UUID eventId, String feideId, LocalDateTime createdAt){
        this.eventId=eventId;
        this.feideId=feideId;
        this.createdAt=createdAt;

    }

    //getters
    public UUID getEventId() {
        return eventId;
    }

    public String getFeideId() {
        return feideId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    //setters

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setFeideId(String feideId) {
        this.feideId = feideId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals (Object o){
        if (this == o ) return true;
        if(!(o instanceof EventRegId)) return false;
        EventRegId that = (EventRegId) o;
        return Objects.equals(eventId,that.eventId) &&
                Objects.equals(feideId,that.feideId) &&
                Objects.equals(createdAt,that.createdAt);
    }

    @Override
    public int hashCode(){
        return Objects.hash(eventId,feideId,createdAt);
    }
}
