package com.husyairi.ManaProgressAku.DTO.Activity;

import com.husyairi.ManaProgressAku.Entity.Model.Session;

public class InsertActivityResponse {

    private String activityID;
    private Session session;
    private Integer exerciseID;
    private Integer sets;
    private String rep;
    private String weight;

    public InsertActivityResponse(String activityID, Session session, Integer exerciseID, Integer sets, String rep, String weight) {
        this.activityID = activityID;
        this.session = session;
        this.exerciseID = exerciseID;
        this.sets = sets;
        this.rep = rep;
        this.weight = weight;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public Session getSession() {
        return session;
    }

    public void setSessionID(Session session) {
        this.session = session;
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

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
