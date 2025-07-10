package com.husyairi.ManaProgressAku.Entity.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String activityID;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String sessionID;

    @Column(nullable = false)
    private Integer exerciseID;

    @Column
    private Integer sets;

    @Column(columnDefinition = "VARCHAR(20)")
    private String rep;

    @Column(columnDefinition = "VARCHAR(20)")
    private String weight;

    public Activity(){}

    public Activity(String activityID, String sessionID, Integer exerciseID, Integer sets, String rep, String weight) {
        this.activityID = activityID;
        this.sessionID = sessionID;
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
