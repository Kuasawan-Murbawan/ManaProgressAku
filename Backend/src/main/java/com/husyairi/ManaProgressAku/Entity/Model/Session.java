package com.husyairi.ManaProgressAku.Entity.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

// TODO: can we generate ID consist of string and integer

@Entity
@Table(name = "session")
public class Session {

    @Id
    @Column(columnDefinition = "VARCHAR(20)")
    private String sessionID;

    @Column(columnDefinition = "INTEGER")
    private Integer exerciseID;

    @Column(nullable = false)
    private LocalDate date;

    private Integer sets;

    private String weight;

    private String rep;

    public Session() {
    }

    public Session(Integer exerciseID, LocalDate date, Integer sets, String weight, String rep) {
        this.exerciseID = exerciseID;
        this.date = date;
        this.sets = sets;
        this.weight = weight;
        this.rep = rep;
    }

    public Session(String sessionID, Integer exerciseID, LocalDate date, Integer sets, String weight, String rep) {
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
        this.date = date;
        this.sets = sets;
        this.weight = weight;
        this.rep = rep;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }
}
