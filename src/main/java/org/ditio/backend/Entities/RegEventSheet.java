package org.ditio.backend.Entities;

import jakarta.persistence.*;
import org.ditio.backend.Enums.FoodPreference;

@Entity
@Table(name = "reg_event_sheet")
public class RegEventSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reg_id;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "event_id", referencedColumnName = "event_id"),
            @JoinColumn(name = "feide_id", referencedColumnName = "feide_id")
    })
    private EventReg eventReg;

    @Column(name = "study_programme")
    private String studyProgramme;

    @Column(name = "study_year")
    private int studyYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_preference")
    private FoodPreference foodPreference = FoodPreference.NONE;

    public RegEventSheet() {}

    public RegEventSheet(String studyProgramme, int studyYear, FoodPreference foodPreference) {
        this.studyProgramme = studyProgramme;
        this.studyYear = studyYear;
        this.foodPreference = foodPreference;
    }

    public Long getId() { return reg_id; }

    public EventReg getEventReg() { return eventReg; }
    public void setEventReg(EventReg eventReg) { this.eventReg = eventReg; }

    public String getStudyProgramme() { return studyProgramme; }
    public void setStudyProgramme(String studyProgramme) { this.studyProgramme = studyProgramme; }

    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }

    public FoodPreference getFoodPreference() { return foodPreference; }
    public void setFoodPreference(FoodPreference foodPreference) { this.foodPreference = foodPreference; }
}