package com.husyairi.ManaProgressAku.DTO.ActivitySet;

import com.husyairi.ManaProgressAku.Entity.Model.Activity;

import java.math.BigDecimal;

public class GetSetResponse {

    private Long setID;

    private BigDecimal weight;

    private Integer reps;

    private Integer setNumber;

    private Activity activity;

    public GetSetResponse() {
    }

    public GetSetResponse(Long setID, BigDecimal weight, Integer reps, Integer setNumber, Activity activity) {
        this.setID = setID;
        this.weight = weight;
        this.reps = reps;
        this.setNumber = setNumber;
        this.activity = activity;
    }

    public Long getSetID() {
        return setID;
    }

    public void setSetID(Long setID) {
        this.setID = setID;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
