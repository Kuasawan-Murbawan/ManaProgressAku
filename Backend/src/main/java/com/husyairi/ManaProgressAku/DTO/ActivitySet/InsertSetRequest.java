package com.husyairi.ManaProgressAku.DTO.ActivitySet;

import java.math.BigDecimal;

public class InsertSetRequest {

    private BigDecimal weight;

    private Integer reps;

    private Integer setNumber;

    private String activityID;

    public InsertSetRequest() {
    }

    public InsertSetRequest(BigDecimal weight, Integer reps, Integer setNumber, String activityID) {
        this.weight = weight;
        this.reps = reps;
        this.setNumber = setNumber;
        this.activityID = activityID;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
}
