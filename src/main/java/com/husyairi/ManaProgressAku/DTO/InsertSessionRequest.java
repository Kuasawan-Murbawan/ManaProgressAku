package com.husyairi.ManaProgressAku.DTO;

import java.time.LocalDate;

public class InsertSessionRequest {

    // we dont specify sessionID because we will generate it in the service

    private Integer exerciseID;

    private LocalDate date;

    private Integer sets;

    private String weight;

    private String rep;

    public InsertSessionRequest() {
    }

    public InsertSessionRequest(Integer exerciseID, LocalDate date) {
        this.exerciseID = exerciseID;
        this.date = date;
    }

    public InsertSessionRequest(Integer exerciseID, LocalDate date, Integer sets, String weight, String rep) {
        this.exerciseID = exerciseID;
        this.date = date;
        this.sets = sets;
        this.weight = weight;
        this.rep = rep;
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
