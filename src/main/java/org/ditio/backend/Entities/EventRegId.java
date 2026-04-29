package org.ditio.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EventRegId implements Serializable {

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "feide_id")
    private String feideId;

    public EventRegId() {}

    public EventRegId(UUID eventId, String feideId) {
        this.eventId = eventId;
        this.feideId = feideId;
    }

    public UUID getEventId() { return eventId; }
    public String getFeideId() { return feideId; }

    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public void setFeideId(String feideId) { this.feideId = feideId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRegId)) return false;
        EventRegId that = (EventRegId) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(feideId, that.feideId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, feideId);
    }
}
