package com.husyairi.ManaProgressAku.DTO.Activity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class InsertActivityRequest {

    @Schema(example = "SESS003")
    private String sessionID;

    @Schema(example = "4", description = "ID for the particular exercise")
    private Integer exerciseID;

    public InsertActivityRequest(String sessionID, Integer exerciseID) {
        this.sessionID = sessionID;
        this.exerciseID = exerciseID;
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
