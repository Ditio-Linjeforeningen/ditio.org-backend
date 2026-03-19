package org.ditio.backend.Entities;

import jakarta.persistence.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //claude sier man må ha dette pga. hibernate
    @Column(name = "event_id")
    private UUID eventId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private Integer maxAttendees;

    private boolean isPublished;

    //Constructors
    public Event() {}

    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, Integer maxAttendees, boolean isPublished) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.isPublished = isPublished;
    }

    //Getters & setters
    public UUID getEventId() {return eventId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}

    public Integer getMaxAttendees() {return maxAttendees;}
    public void setMaxAttendees(Integer maxAttendees) {this.maxAttendees = maxAttendees;}

    public boolean getIsPublished() {return isPublished;}
    public void setIsPublished(boolean  isPublished) {this.isPublished = isPublished;}

}
