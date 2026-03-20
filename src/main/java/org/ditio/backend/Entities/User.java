package org.ditio.backend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    @Column (nullable = true)
    private boolean quarantine_status;
    
    @OneToOne(mappedBy = "user")
    private Quarantine quarantine;


// Avledet egenskap – ikke i user-DB, men vises i objekter/JSON
    @Transient
    @JsonProperty("quarantine_status")
    public boolean isQuarantineStatus() {
        return quarantine != null && quarantine.getQuarantine_Status();
    }

    
    public User() {}

    public User(String feideId, String navn, String email) {
        this.feideId = feideId;
        this.navn = navn;
        this.email = email;
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

}
