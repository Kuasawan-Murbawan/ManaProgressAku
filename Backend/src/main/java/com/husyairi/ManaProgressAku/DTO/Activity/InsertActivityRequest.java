package com.husyairi.ManaProgressAku.DTO.Activity;

import io.swagger.v3.oas.annotations.media.Schema;

public class InsertActivityRequest {

    @Schema(example = "SESS003")
    private String sessionID;

    @Schema(example = "4", description = "ID for the particular exercise")
    private Integer exerciseID;

    @Schema(example = "3", description = "3 sets of workout")
    private Integer sets;

    @Schema(example = "10,10,10", description = "Weight of the equipment in KG")
    private String weight;

    @Schema(example = "8,8,8", description = "Reps for each set")
    private String rep;

    public InsertActivityRequest( String sessionID, Integer exerciseID, Integer sets, String weight, String rep) {
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
        this.sets = sets;
        this.weight = weight;
        this.rep = rep;
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
