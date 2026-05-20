package org.ditio.backend.Entities;

import java.time.LocalDateTime;

import org.ditio.backend.Entities.Enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    @Column(name = "quarantine_until")
    private LocalDateTime quarantineUntil;
    
    //enum for rolle
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role = UserRole.USER; 

    public User() {}

    public User(String feideId, String navn, String email, UserRole role, LocalDateTime quarantine_until ) {
        this.feideId = feideId;
        this.navn = navn;
        this.email = email;
        this.role = role;
        this.quarantineUntil = quarantine_until;
    }

    //Getters og Setters
    public String getEmail() { return email; }
    public void setEmail(String email) {  this.email = email; }

    public String getFeideId() {  return feideId; }

    public String getNavn() { return navn; }
    public void setNavn(String navn) {  this.navn = navn; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getQuarantine_until() {return quarantineUntil;}
    public void setQuarantine_until(LocalDateTime quarantine_until) 
    {this.quarantineUntil = quarantine_until;}

}