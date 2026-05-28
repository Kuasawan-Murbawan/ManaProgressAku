package com.husyairi.ManaProgressAku.DTO.Activity;

import java.time.LocalDateTime;

public class GetActivityResponse {

    private String activityID;
    private String sessionID;
    private Integer exerciseID;
    private LocalDateTime created_at;

    public GetActivityResponse(String activityID, String sessionID, Integer exerciseID, LocalDateTime created_at) {
        this.activityID = activityID;
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
        this.created_at = created_at;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
