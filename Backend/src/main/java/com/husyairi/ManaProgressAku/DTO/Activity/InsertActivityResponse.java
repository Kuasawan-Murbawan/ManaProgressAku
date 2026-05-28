package com.husyairi.ManaProgressAku.DTO.Activity;

import com.husyairi.ManaProgressAku.Entity.Model.Session;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class InsertActivityResponse {

    private String activityID;
    private Session session;

    @Schema(example = "4", description = "ID for the particular exercise")
    private Integer exerciseID;

    private LocalDateTime created_at;


    public InsertActivityResponse(String activityID, Session session, Integer exerciseID, LocalDateTime created_at) {
        this.activityID = activityID;
        this.session = session;
        this.exerciseID = exerciseID;
        this.created_at = created_at;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
