package org.ditio.backend.Entities;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="quarantine")
public class Quarantine {

    @Id
    @GeneratedValue
    private UUID quarantine_id;

    @Column(nullable = false)
    private boolean quarantine_status;

    @Column(nullable = false)
    private LocalDate quarantine_end;

    /*@Column(nullable = false)
    private LocalDate attendance_deadline;*/


    //Quarantine er FK inni User.java
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;
    
   
    public Quarantine() {}

    public Quarantine(boolean quarantine_status, LocalDate quarantine_end, User user /* , LocalDate attendance_deadline*/){
        this.quarantine_status=quarantine_status;
        this.quarantine_end=quarantine_end;
        this.user=user;
       // this.attendance_deadline=attendance_deadline;
    }

    public boolean getQuarantine_Status(){
        return quarantine_status;
    }

    public void setQuranatine_Status(boolean quarantine_status){
        this.quarantine_status=quarantine_status;
    }

    public LocalDate getQuarantine_end(){
        return quarantine_end.plusMonths(1);
    }

    public void setQuarantine_end(LocalDate quarantine_end){
        this.quarantine_end=quarantine_end;
    }

    public String getFeideId(){
        return user != null ? user.getFeideId() : null;
    }
    //For testing hardkodet
    /*public LocalDate getAttendance_deadline(){
        return attendance_deadline;
    }

    public void setAttendance_deadline(LocalDate attendance_deadline){
        this.attendance_deadline=attendance_deadline;
    }*/

    }
    


//https://www.baeldung.com/jpa-one-to-one 
// //Legg til fremmednøkkel for Attendance.java (for Event_id) fra: <---- Sign_up.java <---- Event.java
    // ...