package org.ditio.backend.Entities;

import jakarta.persistence.*;

import java.util.UUID;

import org.ditio.backend.Enums.Attendance_Values;

@Entity
    @Table(name = "event_reg2")
    public class EventReg2 {

    @Id
    @GeneratedValue
    private UUID event_reg_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Event event;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Attendance_Values att_status; 

    public EventReg2() {}

    public EventReg2(UUID event_reg_id, /*String feideId,*/ Attendance_Values att_status) {
        this.event_reg_id = event_reg_id;
        //this.feideId = feideId;
        this.att_status = att_status;  
    }

    public Attendance_Values getAttStatus(){return att_status;}
    public void setAttStatus(Attendance_Values att_status){ this.att_status = att_status;}

}