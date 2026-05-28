package com.husyairi.ManaProgressAku.DTO.Activity;

public class UpdateActivityRequest {
    private String activityID;
    private String sessionID;
    private Integer exerciseID;

    public UpdateActivityRequest(String activityID, String sessionID, Integer exerciseID) {
        this.activityID = activityID;
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
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

}
