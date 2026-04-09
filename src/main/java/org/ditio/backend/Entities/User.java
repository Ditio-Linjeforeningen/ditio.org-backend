package org.ditio.backend.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    //@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, /*or refresh*/ optional = true, orphanRemoval = true)
    /*@JoinColumn(name = "quarantine", referencedColumnName = "quarantine_id", nullable = true)
    private Quarantine quarantine;*/

    //Quarantine.java er FK til User.java
    /*@OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    @JoinColumn(name = "quarantine")
    private Quarantine quarantine; */


    
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

    /*public Quarantine getQuarantine() { return quarantine; }
    //public void setQuarantine(Quarantine quarantine) { this.quarantine = quarantine; }
*/
}
//https://medium.com/@myggona/spring-boot-persistence-context-b112bc7382df
//https://www.baeldung.com/spring-data-jpa-refresh-fetch-entity-after-save
//https://medium.com/@ndhamani2002/handling-outdated-data-in-jpa-persistence-context-after-native-queries-ea1b10ce1d88
