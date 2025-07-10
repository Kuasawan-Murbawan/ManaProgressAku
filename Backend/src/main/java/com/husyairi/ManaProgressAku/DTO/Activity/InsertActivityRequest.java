package com.husyairi.ManaProgressAku.DTO.Activity;

public class InsertActivityRequest {

    private String activityID;
    private String sessionID;
    private Integer exerciseID;
    private Integer sets;
    private String weight;
    private String rep;

    public InsertActivityRequest(String activityID, String sessionID, Integer exerciseID, Integer sets, String weight, String rep) {
        this.activityID = activityID;
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
        this.sets = sets;
        this.weight = weight;
        this.rep = rep;
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
