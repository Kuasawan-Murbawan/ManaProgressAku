package com.husyairi.ManaProgressAku.DTO.Activity;

import com.husyairi.ManaProgressAku.Entity.Model.Session;
import io.swagger.v3.oas.annotations.media.Schema;

public class InsertActivityResponse {

    private String activityID;
    private Session session;

    @Schema(example = "4", description = "ID for the particular exercise")
    private Integer exerciseID;

    @Schema(example = "3", description = "3 sets of workout")
    private Integer sets;

    @Schema(example = "8,8,8", description = "Reps for each set")
    private String rep;

    @Schema(example = "10,10,10", description = "Weight of the equipment in KG")
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
