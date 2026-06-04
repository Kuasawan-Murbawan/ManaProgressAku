package com.husyairi.ManaProgressAku.DTO.ActivitySet;

import java.math.BigDecimal;

public class SetDetailsResponse {

    private Long setID;
    private Integer setNumber;
    private BigDecimal weight;
    private Integer reps;

    public SetDetailsResponse(Long setID, Integer setNumber, BigDecimal weight, Integer reps) {
        this.setID = setID;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
    }

    public Long getSetID() {
        return setID;
    }

    public void setSetID(Long setID) {
        this.setID = setID;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
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
}
