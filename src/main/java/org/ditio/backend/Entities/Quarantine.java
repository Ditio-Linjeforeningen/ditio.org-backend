package org.ditio.backend.Entities;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="quarantine")
public class Quarantine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "quarantine_id")
    private UUID quarantine_id;

    @Column(nullable = false)
    private boolean does_quarantine_exist;

    @Column(nullable = false)
    private LocalDate quarantine_end;

    //bidireksjonal 1:1 forhold
    @OneToOne(mappedBy = "quarantine", fetch = FetchType.LAZY)
    private User user;

    //Legg til FK for Attendance for Event-id (dato for q-slutt) fra sign-up
   
    public Quarantine(boolean b, LocalDate quarantineEnd) {}

    public Quarantine(boolean does_quarantine_exist, LocalDate quarantine_end, User user){
        this.does_quarantine_exist=does_quarantine_exist;
        this.quarantine_end=quarantine_end;
        this.user = user;
    }

    public boolean getDoes_quarantine_exist(){
        return does_quarantine_exist;
    }
    public void setDoes_quarantine_exist(boolean does_quarantine_exist){
        this.does_quarantine_exist=does_quarantine_exist;
    }
    public LocalDate getQuarantine_end(){
        //Adds one month of quarantine from the event-date
        return quarantine_end.plusMonths(1); 
    }
    public void setQuarantine_end(LocalDate quarantine_end){
        this.quarantine_end=quarantine_end;
    }


    }
    


//https://www.baeldung.com/jpa-one-to-one 
// //Legg til fremmednøkkel for Attendance.java (for Event_id) fra: <---- Sign_up.java <---- Event.java
    // ...