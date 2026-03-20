package org.ditio.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private String feideId;

    @Column(name = "navn")
    private String navn;

    @Column(name = "email")
    private String email;

    @Column (name = "quarantine_status", nullable = false)
    private boolean quarantine_status;
    
    @OneToOne(mappedBy = "user")
    private Quarantine quarantine;

    public User() {}

    public User(String feideId, String navn, String email, boolean quarantine_status) {
        this.feideId = feideId;
        this.navn = navn;
        this.email = email;
        this.quarantine_status=quarantine_status;
    }

    //Getters og Setters

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeideId() {
        return feideId;
    }

    public void setFeideId(String feideId) {
        this.feideId = feideId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public boolean getQuarantine_Status(){
        return quarantine != null ? quarantine.getQuarantine_Status() : null;
    }
    //Setter når det er inni Quarantine.java?

}
