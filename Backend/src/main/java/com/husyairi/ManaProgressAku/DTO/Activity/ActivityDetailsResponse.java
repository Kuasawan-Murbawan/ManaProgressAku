package com.husyairi.ManaProgressAku.DTO.Activity;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.SetDetailsResponse;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityDetailsResponse {
    private String activityID;
    private Integer exerciseID;
    private LocalDateTime createdAt;
    private List<SetDetailsResponse> sets;

    public ActivityDetailsResponse(String activityID, Integer exerciseID, LocalDateTime createdAt, List<SetDetailsResponse> sets) {
        this.activityID = activityID;
        this.exerciseID = exerciseID;
        this.createdAt = createdAt;
        this.sets = sets;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SetDetailsResponse> getSets() {
        return sets;
    }

    public void setSets(List<SetDetailsResponse> sets) {
        this.sets = sets;
    }
}
