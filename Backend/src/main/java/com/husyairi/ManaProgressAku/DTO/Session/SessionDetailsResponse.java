package com.husyairi.ManaProgressAku.DTO.Session;

import com.husyairi.ManaProgressAku.DTO.Activity.ActivityDetailsResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SessionDetailsResponse {

    private String sessionID;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private List<ActivityDetailsResponse> activities;

    public SessionDetailsResponse(String sessionID, String status, LocalDate date, LocalTime time, List<ActivityDetailsResponse> activities) {
        this.sessionID = sessionID;
        this.status = status;
        this.date = date;
        this.time = time;
        this.activities = activities;
    }

    public String getSesionID() {
        return sessionID;
    }

    public void setSesionID(String sesionID) {
        this.sessionID = sesionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<ActivityDetailsResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDetailsResponse> activities) {
        this.activities = activities;
    }
}
